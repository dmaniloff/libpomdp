'''
 libpomdp
 ========
 File: genscript.py
 Description: python script to generate catch pomdp in
              Cassandra's format
 Authors: Silvano and Diego.
'''

# imports
import random;

# declarations
rows = 3;                       # rows
cols = 3;                       # cols
n = rows * cols;                # squares
k = 2;                          # agents
w = 1;                          # wumpi
a = 5;                          # actions per agent
o = 2;                          # observations per agent
mr = 0.8;                       # motion reliability
obsn = ['wp', 'wa'];
actn = ['N', 'S', 'E', 'W', 'T'];

# state numbering function
def n2dec(p, n, k):
    i = 0;
    for j in range(k):
        i += n**j * p[j];
    return i;

# state decoding function
def dec2n(i, n, k):
    p = [0] * k;
    j = k-1;
    while i >= n:
        p[j] = i % n;
        i = i // n;
        j = j - 1;
    p[j] = i;
    return p;

# state encoding to string
def vec2str(p, names=[]):
    s = '';
    if [] == names:
        for pos in p:
            s = s + '_' + str(pos);
    else:
        for pos in p:            
            s = s + '_' + names[pos]; 
    return s;

# deterministic successor-state functions for a single agent
def north(state, agent, n, k, rows, cols):
    p = dec2n(state, n, k);
    pos = decpos(p[agent], rows, cols);
    if pos[0] == rows - 1:
        return state;
    else:
        p[agent] = encpos(pos[0] + 1, pos[1], rows, cols);
        return n2dec(p, n, k);

def south(state, agent, n, k, rows, cols):
    p = dec2n(state, n, k);
    pos = decpos(p[agent], rows, cols);
    if pos[0] == 0:
        return state;
    else:
        p[agent] = encpos(pos[0] - 1, pos[1], rows, cols);
        return n2dec(p, n, k);

def east(state, agent, n, k, rows, cols):
    p = dec2n(state, n, k);
    pos = decpos(p[agent], rows, cols);
    if pos[1] == cols - 1:
        return state;
    else:
        p[agent] = encpos(pos[0], pos[1] + 1, rows, cols);
        return n2dec(p, n, k);

def south(state, agent, n, k, rows, cols):
    p = dec2n(state, n, k);
    pos = decpos(p[agent], rows, cols);
    if pos[1] == 0:
        return state;
    else:
        p[agent] = encpos(pos[0], pos[1] - 1, rows, cols);
        return n2dec(p, n, k);

def add2dict(dic, key, val):
    """Add an entry to a dictionary
    If dictionary already has that key the values are added together.

    Keyword arguments:
    dic -- the dictionary
    key -- the key
    val -- the value associated to key

    """

    if dic.has_key(key):
        dic[key] += val;
    else:
        dic[key] = val;

# Merge two dictionaries.
def merge2dict(dic, m):
    """Merge entries of dictionary m into dictionary dic.
    Duplicated entries are summed together.

    Keyword arguments:
    dic -- the dictionary
    m   -- the dictionary to merge into dic

    """
    for k,v in m:
        add2dict(dic, k, v)

# Transition function for a single agent action
def stransition(state, agent, action, mr, n, k, rows, cols):
    """Transition function for a single agent action.
    Returns a dictionary <next_state,prob>

    Keyword arguments:
    state  -- the state number
    agent  -- the index of the agent performing the action
    action -- the index of the action (0='N', 1='S', 2='E', 3='W', 4='T')
    mr     -- the reliability of movement actions
    n      -- the number of squares in the world
    k      -- the number of agents in the world
    rows   -- the number of rows in the world
    cols   -- the number of cols in the world

    """

    nstated = dict();
    # North
    if action == 0:
        nstated[north(state, agent, n, k, rows, cols)] = mr;
        add2dict(nstated, west(state, agent, n, k, rows, cols), (1.0-mr)/2.0);
        add2dict(nstated, east(state, agent, n, k, rows, cols), (1.0-mr)/2.0);
    # South
    elif action == 1:
        nstated[south(state, agent, n, k, rows, cols)] = mr;
        add2dict(nstated, west(state, agent, n, k, rows, cols), (1.0-mr)/2.0);
        add2dict(nstated, east(state, agent, n, k, rows, cols), (1.0-mr)/2.0);
    # East
    elif action == 2:
        nstated[east(state, agent, n, k, rows, cols)] = mr;
        add2dict(nstated, north(state, agent, n, k, rows, cols), (1.0-mr)/2.0);
        add2dict(nstated, south(state, agent, n, k, rows, cols), (1.0-mr)/2.0);
    # West
    elif action == 3:
        nstated[west(state, agent, n, k, rows, cols)] = mr;
        add2dict(nstated, north(state, agent, n, k, rows, cols), (1.0-mr)/2.0);
        add2dict(nstated, south(state, agent, n, k, rows, cols), (1.0-mr)/2.0);
    # Tag
    elif action == 4:
        nstated[state] = 1.0;
    # Should never happen
    else:
        nstated[state] = 1.0;

    return nstated;

# Transition function for joint action
def jtransition(state, ja, mr, n, k, rows, cols):
    """Transition function for joint action.
    Returns a dictionary <next_state,prob>

    Keyword arguments:
    state  -- the state number
    ja     -- the joint action, as a vector [a_1, ..., a_k]
    mr     -- the reliability of movement actions
    n      -- the number of squares in the world
    k      -- the number of agents in the world
    rows   -- the number of rows in the world
    cols   -- the number of cols in the world

    """
    
    stated = dict();
    stated[state] = 1.0;
    return rtransition(stated, ja, 0, mr, n, k, rows, cols);

def rtransition(stated, ja, agent, mr, n, k, rows, cols):
    nstated = dict();
    if agent == k-1:
        for s,p in stated.iteritems():
            merge2dic(nstated, stransition(s, agent, ja[agent], mr, n, k, rows, cols));
    else:
        for s,p in stated.iteritems():
            merge2dic(nstated, rtransition(nstated, ja, agent+1, mr, n, k, rows, cols));
    
    return nstated;

# decode position: (row, col)
def decpos(pos, rows, cols):
    return [pos // cols, pos % cols];
    
# encode agent's position
def encpos(r, c, rows, cols):
    return r * cols + c;

# open file
f = open('catchSimple.POMDP', 'w');

# init cassandra file
f.write('discount: 0.95\n');
f.write('values: reward\n');
f.write('states: ')

# enumerate states - the wumpi are the least significat digits - agent0 is the most significant digit
for i in range(n**(k+w)):
    f.write(vec2str(dec2n(i,n,k+w)) + ' ');
f.write('\n');

# enumerate actions
f.write('actions: ')
for i in range(a**k):
    f.write(vec2str(dec2n(i,a,k), actn) + ' ');
f.write('\n');

# enumerate observations
f.write('observations: ');
for i in range(o**k):
    f.write(vec2str(dec2n(i,o,k), obsn) + ' ');
f.write('\n');

# start state
f.write('start: \n');
rp = round(1.0/n, 6);
for i in range(n**k):
    if i < n-1:
        f.write(str(rp) + ' ');
    elif i == n-1:
        f.write(str(1.0-(n-1.0)*rp) + ' ');
    else:
        f.write('0 ');
f.write('\n');
        
# transition function
f.write('T: N\n')
#for i in range(n**k):
    

