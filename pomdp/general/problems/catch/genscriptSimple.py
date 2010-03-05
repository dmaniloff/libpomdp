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




# classes
class AgentInterface:
    """ 
    general agent class for agents and wumpi
    """
    
    def states(self):
        return 1;

    def actions(self):
        return 1;

    def transition(self, state, action):
        return dict();

    def actstr(self, acti):
        return '0';

class Agent(AgentInterface):
    """ 
    acting agent for the catch environment
    """

    def __init__(self, rows, cols, mr):
        """
        constructor
        """
        self.rows = rows;
        self.cols = cols;
        self.n    = rows * cols;
        #self.actn = ['N', 'S', 'E', 'W', 'T'];
        self.actn = ['k'];
        self.a    = len(self.actn);
        self.mr   = mr;

    def states(self):
        return self.n;

    def actions(self):
        return self.a;

    def actstr(self, acti):
        return self.actn[acti];

    def transition(self, agentpos, action):
        """
        Nondeterminisic transition function for a single agent action.
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
            nstated[north(agentpos, self.rows, self.cols)] = self.mr;
            add2dict(nstated, west(agentpos, self.rows, self.cols), (1.0-self.mr)/2.0);
            add2dict(nstated, east(agentpos, self.rows, self.cols), (1.0-self.mr)/2.0);
        # South
        elif action == 1:
            nstated[south(agentpos, self.rows, self.cols)] = self.mr;
            add2dict(nstated, west(agentpos, self.rows, self.cols), (1.0-self.mr)/2.0);
            add2dict(nstated, east(agentpos, self.rows, self.cols), (1.0-self.mr)/2.0);
        # East
        elif action == 2:
            nstated[east(agentpos, self.rows, self.cols)] = self.mr;
            add2dict(nstated, north(agentpos, self.rows, self.cols), (1.0-self.mr)/2.0);
            add2dict(nstated, south(agentpos, self.rows, self.cols), (1.0-self.mr)/2.0);
        # West
        elif action == 3:
            nstated[west(agentpos, self.rows, self.cols)] = mr;
            add2dict(nstated, north(agentpos, self.rows, self.cols), (1.0-mr)/2.0);
            add2dict(nstated, south(agentpos, self.rows, self.cols), (1.0-mr)/2.0);
        # Tag
        elif action == 4:
            nstated[agentpos] = 1.0;
        # Should never happen
        else:
            nstated[agentpos] = 1.0;            
        return nstated;

class StupidWumpus(AgentInterface):
    """
    simple random-moving wumpus
    """
    def __init__(self, rows, cols):
        """
        constructor
        """
        self.rows = rows;
        self.cols = cols;
        self.n    = rows * cols;
    
    def states(self):
        return n;
    
    def transition(self, wumpuspos, action):
        nstated = dict();
        nstated[north(wumpuspos, self.rows, self.cols)] = 0.25;
        add2dict(nstated, south(wumpuspos, self.rows, self.cols), 0.25);
        add2dict(nstated, east (wumpuspos, self.rows, self.cols), 0.25);
        add2dict(nstated, west (wumpuspos, self.rows, self.cols), 0.25);
        return nstated;

# deterministic successor-state functions for a single agent
def north(agentpos, rows, cols):
    # get row and col position for the agent
    pos = decpos(agentpos, rows, cols);
    if pos[0] == rows - 1:
        return agentpos;
    else:
        return encpos(pos[0] + 1, pos[1], rows, cols);

def south(agentpos, rows, cols):
    # get row and col position for the agent
    pos = decpos(agentpos, rows, cols);
    if pos[0] == 0:
        return agentpos;
    else:
        return encpos(pos[0] - 1, pos[1], rows, cols);

def east(agentpos, rows, cols):
    # get row and col position for the agent
    pos = decpos(agentpos, rows, cols);
    if pos[1] == cols - 1:
        return agentpos;
    else:
        return encpos(pos[0], pos[1] + 1, rows, cols);

def west(agentpos, rows, cols):
    # get row and col position for the agent
    pos = decpos(agentpos, rows, cols);
    if pos[1] == 0:
        return agentpos;
    else:
        return encpos(pos[0], pos[1] - 1, rows, cols);

def n2dec(p, n, k):
    '''
    base n into decimal conversioin
    '''
    i = 0;
    for j in range(k):
        i += n**j * p[k-j-1];
    return i;

def dec2n(i, n, k):
    '''
    state encoding into base n
    '''
    p = [0] * k;
    j = k-1;
    while i >= n:
        p[j] = i % n;
        i = i // n;
        j = j - 1;
    p[j] = i;
    return p;

def ja2str(av, agents):
    s='';
    for ai in range(len(agents)):
        s = s + '_' + agents[ai].actstr(av[ai]);
    return s;

def vec2str(p, names=[]):
    """
    state encoding to string
    """
    s = '';
    if [] == names:
        for pos in p:
            s = s + '_' + str(pos);
    else:
        for pos in p:            
            s = s + '_' + names[pos]; 
    return s;



def add2dict(dic, key, val):
    """
    Add an entry to a dictionary
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


# decode position: (row, col)
def decpos(pos, rows, cols):
    return [pos // cols, pos % cols];
    
# encode agent's position
def encpos(r, c, rows, cols):
    return r * cols + c;

def decja(ja, agents):
    """
    decode joint action into vector of individual actions
    """
    na  = len(agents);
    jav = [0] * na;
    ai  = na-1;
    while ja > 0:
        jav[ai] = ja % agents[ai].actions();
        ja = ja // agents[ai].actions();
        ai = ai - 1;
    return jav;

def decjs(js, agents):
    """
    decode joint state into vector of individual actions
    """
    na  = len(agents);
    jsv = [0] * na;
    ai  = na-1;
    while js > 0:
        jsv[ai] = js % agents[ai].states();
        js = js // agents[ai].states();
        ai = ai - 1;
    return jsv;

def decode(j, dimensions):
    """
    general decode
    """
    nd  = len(dimensions);
    jv = [0] * nd;
    di  = nd-1;
    while j > 0:
        jv[di] = j % dimensions[di];
        j = j // dimensions[di];
        di = di - 1;
    return jv;

def encode(jv, dimensions):
    """
    general encode
    """
    nd = len(jv);
    j = 0;
    f = 1;
    i = nd - 1;
    while i >= 0:
        j += f * jv[i];
        f *= dimensions[i];
        i = i - 1;

    return j;

def jp(jsv, jav, agents):
    # next states' probability dictionary
    nspd = dict();
    # first build dimensions array
    dimensions = [];
    jns = 1;
    for i in range(len(agents)):        
        dimensions.append(len(nstates[agents[i]][jsv[i]][jav[i]]));
        # compute number of joint next states
        jns *= dimensions[i];
    
    p = 1.0;
    for i in range(jns):
        # obtain dictionary indices
        di = decode(i, dimensions);
        # next state array
        ns = [];
        for ai in range(len(agents)):
            ns.append(nstates[agents[ai]][jsv[ai]][jav[ai]].keys()[di[ai]]);
            p *= nstates[agents[ai]][jsv[ai]][jav[ai]][ns[ai]];
        nspd[encode(ns, statearity)] = p;
        return nspd;

###############################################################################
# computations
###############################################################################

# declarations
rows = 2;                       # rows
cols = 2;                       # cols
n    = rows * cols;             # squares
k    = 2;                       # agents
w    = 0;                       # wumpi
o    = 2;                       # observations per agent
mr   = 0.8;                     # motion reliability
obsn = ['wp', 'wa'];
agents = [Agent(rows, cols, mr)] * k + [StupidWumpus(rows, cols)] * w;

actionarity = [];
statearity = [];
for ai in range(len(agents)):
    actionarity.append(agents[ai].actions());
    statearity.append(agents[ai].states());

# dictionaries of individual s'
nstates = dict();
totja = 1;
totjs = 1;
for ag in agents:
    totja *= ag.actions();
    totjs *= ag.states();
    nstates[ag] = dict();
    for s in range(ag.states()):
        nstates[ag][s] = dict();
        for a in range(ag.actions()):
            nstates[ag][s][a] = ag.transition(s, a);
print(totja)
###############################################################################
# start writing the POMDP file
###############################################################################

# open file
f = open('catchSimple.POMDP', 'w');

# init parameters
f.write('discount: 0.95\n');
f.write('values: reward\n');
f.write('states: ')

# enumerate states - the wumpi are the least significat digits - agent0 is the most significant digit
for i in range(n**(k+w)):
    f.write(vec2str(dec2n(i,n,k+w)) + ' ');
f.write('\n');

# enumerate actions
f.write('actions: ')
for ja in range(totja):
    f.write(ja2str(decode(ja, actionarity), agents) + ' ');
f.write('\n');

# enumerate observations
f.write('observations: ');
for i in range(o**k):
    f.write(vec2str(dec2n(i,o,k), obsn) + ' ');
f.write('\n');

# start state
f.write('start: \n');
rp = round(1.0/n, 6);
for i in range(n**(k+w)):
    if i < n-1:
        f.write(str(rp) + ' ');
    elif i == n-1:
        f.write(str(1.0-(n-1.0)*rp) + ' ');
    else:
        f.write('0 ');
f.write('\n');
        
# compute cross product of the dictionaries
f.write('T: * : * : * : 0.0\n');

for ja in range(totja):
    for js in range(totjs):
        jav = decode(ja, actionarity);
        jsv = decode(js, statearity);
        print(jav)
        print(jsv)
        nsp = jp(jsv, jav, agents);
        for ns,p in nsp.iteritems():
            f.write('T: ' + ja2str(jav, agents) + ' : ' + vec2str(jsv) + ' : ' 
                    +  vec2str(decode(ns,statearity)) + ' : ' + str(p) + '\n');

    


