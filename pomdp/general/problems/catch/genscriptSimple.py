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

###############################################################################
# parameters
###############################################################################

# declarations
rows = 2;                       # rows
cols = 2;                       # cols
n    = rows * cols;             # squares
k    = 2;                       # agents
w    = 1;                       # wumpi
mr   = 0.8;                     # motion reliability
discount = 0.95;

###############################################################################
# Classes
###############################################################################
class AgentInterface:
    """ 
    general agent class for agents and wumpi
    """
    
    def __init(self):
        self.astr = self.aname(0);
        self.ostr = self.oname(0);

    def states(self):
        return 1;

    def actions(self):
        return 1;

    def observations(self):
        return 1;

    def stransition(self, state, action):
        return dict();

    def anames(self):
        return self.astr;

    def aname(self, acti):
        return '0';

    def onames(self):
        return self.ostr;
    
    def oname(self, obsi):
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
        self.actn = ['n', 's', 'e', 'w', 't'];
#        self.actn = ['N'];
        self.a    = len(self.actn);
        self.obsn = ['wp', 'wa'];
        self.o    = len(self.obsn);
        self.mr   = mr;
        self.astr = self.actn[0];
        for a in self.actn[1:]:
            self.astr += ' ' + a;
        self.ostr = self.obsn[0];
        for o in self.obsn[1:]:
            self.ostr += ' ' + o;

    def states(self):
        return self.n;

    def actions(self):
        return self.a;

    def observations(self):
        return self.o;

    def aname(self, acti):
        return self.actn[acti];

    def oname(self, obsi):
        return self.obsn[obsi];

    def stransition(self, agentpos, action):
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

        # Next state dictionary
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
    
    def stransition(self, wumpuspos, action):
        nstated = dict();
        nstated[north(wumpuspos, self.rows, self.cols)] = 0.25;
        add2dict(nstated, south(wumpuspos, self.rows, self.cols), 0.25);
        add2dict(nstated, east (wumpuspos, self.rows, self.cols), 0.25);
        add2dict(nstated, west (wumpuspos, self.rows, self.cols), 0.25);
        return nstated;


###############################################################################
# Grid helper functions
###############################################################################

def decpos(pos, rows, cols):
    """ Decode pos --> [row, col]

    Keyword arguments:
    pos  -- position in the grid
    rows -- number of rows in the grid
    cols -- number of columns in the grid

    Numbering in the grid is row-major starting form the bottom row. Eg.:
    
       6 7 8
       3 4 5
       0 1 2

    """
    return [pos // cols, pos % cols];
    
def encpos(r, c, rows, cols):
    """ Encode [r, c] --> pos

    Keyword arguments:
    r    -- row number
    c    -- column number
    rows -- number of rows in the grid
    cols -- number of columns in the grid

    Numbering in the grid is row-major starting form the bottom row. Eg.:
    
       6 7 8
       3 4 5
       0 1 2

    """
    return r * cols + c;

# Functions to move (deterministically) in a grid
def north(spos, rows, cols):
    """ Move north
    If spos is on the top edge of the grid the position doesn't change.

    Keyword arguments:
    spos -- starting position
    rows -- number of rows in the grid
    cols -- number of columns in the grid
    """
    # get row and col position for the agent
    pos = decpos(spos, rows, cols);
    if pos[0] == rows - 1:
        return spos;
    else:
        return encpos(pos[0] + 1, pos[1], rows, cols);

def south(spos, rows, cols):
    """ Move south
    If spos is on the bottom edge of the grid the position doesn't change.

    Keyword arguments:
    spos -- starting position
    rows -- number of rows in the grid
    cols -- number of columns in the grid
    """
    # get row and col position for the agent
    pos = decpos(spos, rows, cols);
    if pos[0] == 0:
        return spos;
    else:
        return encpos(pos[0] - 1, pos[1], rows, cols);

def east(spos, rows, cols):
    """ Move east
    If spos is on the rightmost edge of the grid the position doesn't change.

    Keyword arguments:
    spos -- starting position
    rows -- number of rows in the grid
    cols -- number of columns in the grid
    """
    # get row and col position for the agent
    pos = decpos(spos, rows, cols);
    if pos[1] == cols - 1:
        return spos;
    else:
        return encpos(pos[0], pos[1] + 1, rows, cols);

def west(spos, rows, cols):
    """ Move west
    If spos is on the leftmost edge of the grid the position doesn't change.

    Keyword arguments:
    spos -- starting position
    rows -- number of rows in the grid
    cols -- number of columns in the grid
    """
    # get row and col position for the agent
    pos = decpos(spos, rows, cols);
    if pos[1] == 0:
        return spos;
    else:
        return encpos(pos[0], pos[1] - 1, rows, cols);

def distance(pos1, pos2, rows, cols):
    """ Distance between two cells in a grid

    Returns the distance (row, col) between pos1 and pos2.

    Keyword arguments:
    pos1
    pos2
    rows -- number of rows in the grid
    cols -- number of columns in the grid
    """
    p1 = decpos(pos1, rows, cols);
    p2 = decpos(pos2, rows, cols);
    return (abs(p1[0]-p2[0]), abs(p1[1]-p2[1]));

def colocated(pos1, pos2, rows, cols):
    return pos1 == pos2;

def adjacent(pos1, pos2, rows, cols):
    (r,c) = distance(pos1, pos2, rows, cols);
    return r+c == 1;

###############################################################################
# Encoding/decoding functions
###############################################################################
def n2dec(digits, base):
    """ Convertion to decimal base

    Keyword arguments:
    digits -- the vector of "digits"
    base   -- base of the entries in digits
    """
    num = 0;
    n = len(digits);
    for j in range(n):
        num += base**j * digits[n-j-1];
    return num;

def dec2n(num, base, k):
    """ Convertion from decimal base.

    Return an array whose length is k

    Keyword arguments:
    digits -- the vector of "digits"
    base   -- the new base
    k      -- desired length of the returned array
    """
    digits = [0] * k;
    j = k-1;
    while num >= base:
        digits[j] = num % base;
        num = num // base;
        j = j - 1;
    digits[j] = num;
    return digits;

def decode(num, dimensions):
    """ Decode a number into a vector of "digits".
    
    Return an array whose length is len(dimensions)

    Keyword arguments:
    num        -- the number
    dimensions -- "base" for each "digit"

    A special case for this function is when all entries in dimensions
    are the same, in which case this function is just a change of base
    
    """
    nd  = len(dimensions);
    digits = [0] * nd;
    di  = nd-1;
    while num > 0:
        digits[di] = num % dimensions[di];
        num = num // dimensions[di];
        di = di - 1;
    return digits;

def encode(digits, dimensions):
    """ Encode a vector of "digits" into a number.
    
    Keyword arguments:
    digits     -- the vector of "digits"
    dimensions -- "base" for each "digit"

    A special case for this function is when all entries in dimensions
    are the same, in which case this function is just a change of base    
    """
    nd = len(digits);
    num = 0;
    f = 1;
    i = nd - 1;
    while i >= 0:
        num += f * digits[i];
        f *= dimensions[i];
        i = i - 1;

    return num;

def ja2str(av, agents):
    """ Return a string representation of a joint action

    Keyword arguments:
    av     -- an array of the form [a_1, ..., a_k], where a_i is agent i's action
    agents -- array [A_1, ..., A_k] of agents, used to fetch action names
    """
    s = '';
    for ai in range(len(agents)):
        s += agents[ai].aname(av[ai]) + ' ';
    return s[0:len(s)-1];

def jo2str(ov, agents):
    """ Return a string representation of a joint observation

    Keyword arguments:
    ov     -- an array of the form [o_1, ..., o_k], where o_i is agent i's observation
    agents -- array [A_1, ..., A_k] of agents, used to fetch observation names
    """
    s = '';
    for ai in range(len(agents)):
        s += agents[ai].oname(ov[ai]) + ' ';
    return s[0:len(s)-1];

def vec2str(p, names=[]):
    """
    state encoding to string
    """
    s = 'S';
    if [] == names:
        for pos in p:
            s += str(pos) + '_';
    else:
        for pos in p:            
            s += names[pos] + '_'; 
    return s[0:len(s)-1];

###############################################################################
# Other functions
###############################################################################

def add2dict(dic, key, val):
    """Add an entry to a dictionary.
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

def jtransition(jsv, jav, agents):
    """Transition function.

    Returns a dictionary whose entries are of the form <next_state, probability> and
    correspond to all possible next states (and their probability) when the joint
    action described by jav is performed in the joint state described by jsv.

    Keyword arguments:
    jsv    -- state vector (one state for each agent)
    jav    -- action vector (one action for each agent)
    agents -- array of agents
    
    """

    # This function uses the global variable nstates, which is a 4-dimensional
    # dictionary:
    #    nstates[agt][s][a] is a dictionary whose entries are all possible single-
    #    agent next states for agent agt when it performs action a in state s.
    #    These entries are of the form <next_state, probability>

    # Next states' probability dictionary. We are going to return it
    nspd = dict();

    # Store in dimensions the number of possible next single-agent states for
    # each agent and in n the total number of joint-next-states.
    dimensions = [0]*len(agents);
    n = 1;
    for ai in range(len(agents)):        
        dimensions[ai] = len(nstates[agents[ai]][jsv[ai]][jav[ai]]);
        # compute number of joint next states
        n *= dimensions[ai];

    # Loop on every possible next state: compute its probability and add the tuple
    # to the dictionary nspd.
    for jns in range(n):
        p = 1.0;

        # Obtain dictionary indices: for each agent the single-agent next state that
        # generated the joint next-state jns
        di = decode(jns, dimensions);

        # Compute probability of joint next state by multiplying probabilities of
        # single-agent next states
        ns = [0]*len(agents);
        for ai in range(len(agents)):
            ns[ai] = nstates[agents[ai]][jsv[ai]][jav[ai]].keys()[di[ai]];
            p *= nstates[agents[ai]][jsv[ai]][jav[ai]][ns[ai]];

        # Store <next_state, probability> in the dictionary
        nspd[encode(ns, statearity)] = p;
    
    return nspd;

def jobservation(jav, jsv, ragents, wumpi, rows, cols):
    # Observation probability dictionary. We are going to return it
    nopd = dict();

    # Store in nobs all possible single-agent observations for each agent, 
    # in dimensions the number of such observations and in n the total number 
    # of joint-next-observations.
    dimensions = [1] * (len(ragents) + len(wumpi));
    nobs = [dict()] * len(ragents) + [dict([(0, 1.0)])] * len(wumpi);
    n = 1;

    # check for co-location 
    for rai in range(len(ragents)):
        nobs[rai] = dict();
        rangefl = False;
        for wai in range(len(wumpi)):
            if adjacent(jsv[rai], jsv[len(ragents) + wai], rows, cols):
                rangefl = True;
                break;
        if rangefl == True:
            nobs[rai][0] = 1.0; # 'wp'
        else:
            nobs[rai][1] = 1.0; # 'wa'
        dimensions[rai] = len(nobs[rai]);
        n *= dimensions[rai];
    
    # Loop on every possible next observation: compute its probability and add the tuple
    # to the dictionary nopd.
    for jno in range(n):
        p = 1.0;

        # Obtain dictionary indices: for each agent the single-agent observation
        # that generated the joint obsrvation jno
        di = decode(jno, dimensions);

        # Compute probability of joint observation by multiplying probabilities of
        # single-agent observations
        no = [0] * (len(ragents) + len(wumpi));
        for ai in range((len(ragents) + len(wumpi))):
            no[ai] = nobs[ai].keys()[di[ai]];
            p *= nobs[ai][no[ai]];

        # Store <next_state, probability> in the dictionary
        nopd[encode(no, obsarity)] = p;
    return nopd;

def jreward(jsv, jav, ragents, wumpi, rows, cols):
    # Reward += 1 for each wumpus that is tagged
    reward = 0;
    for wai in range(len(wumpi)):
        for rai in range(len(ragents)):
            if colocated(jsv[rai], jsv[len(ragents) + wai], rows, cols) and jav[rai] == 4:
                reward += 1;
                break;
    return reward;

###############################################################################
# computations
###############################################################################

ragents = [Agent(rows, cols, mr)] * k;
wumpi   = [StupidWumpus(rows, cols)] * w;
agents  = ragents + wumpi;

actionarity = [0] * len(agents);
statearity  = [0] * len(agents);
obsarity    = [0] * len(agents); 
for ai in range(len(agents)):
    actionarity[ai] = agents[ai].actions();
    statearity [ai] = agents[ai].states();
    obsarity   [ai] = agents[ai].observations();

# Compute dictionaries of individual agents' transitions.
# nstates will be  a 4-dimensional dictionary:
#    nstates[ag][s][a] is a dictionary whose entries are all possible single-
#    agent next states for agent ag when it performs action a in state s.
#    These entries are of the form <next_state, probability>
#    nobs[ag][s][a] is a dictionary whose entries are all possible single-
#    agent next observations for agent ag when it performs action a, lands in state s.
#    These entries are of the form <next_obs, probability>

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
            nstates[ag][s][a] = ag.stransition(s, a);

###############################################################################
# start writing the POMDP file
###############################################################################

# open file
fname = 'dcatch' + str(rows) + 'x' + str(cols) + '-' + str(k) + 'a' + str(w) + 'w.dpomdp';
f = open(fname, 'w');

# brief description of the problem
f.write('# Catch problem:\n');
f.write('#\n');
f.write('#    Grid   : ' + str(rows) + 'x' + str(cols) + '\n');
f.write('#    Agents : ' + str(k) + '\n');
f.write('#    Wumpi  : ' + str(w) + '\n');
f.write('#\n');
f.write('#\n');

# init parameters
f.write('# preamble\n');
f.write('agents: ' + str(k) + '\n');
f.write('discount: ' + str(discount) + '\n');
f.write('values: reward\n');
f.write('# States are named Sa1_.._ak_w1_.._wn, where pi is the position of agent i '
        + 'and wi is the position of wumpus i and there are k agents and n wumpi\n');
f.write('states: ')
# enumerate states - the wumpi are the least significat digits - agent0 is the most significant digit
for js in range(totjs):
    jsv = decode(js, statearity);
    f.write(vec2str(jsv) + ' ');
f.write('\n');
f.write('#\n');
# start state
f.write('start:\n');
# Agents start in (0,0) but they don't know where the wumpi are
rp = round(1.0/(n**w), 6);
lp = 1.0;
for js in range(n**w-1):
    f.write(str(rp) + ' ');
    lp -= rp;
f.write(str(lp) + ' ');
for js in range(n**w, totjs):
    f.write('0 ');
f.write('\n');
f.write('#\n');
# enumerate actions
# for each agent we must have one line specifying the agent's actions
f.write('actions:\n');
for ag in ragents:
    f.write(ag.anames() + '\n');
f.write('#\n');
# enumerate observations
# for each agent we must have one line specifying the agent's observations
f.write('observations:\n');
for ag in ragents:
    f.write(ag.onames() + '\n');
f.write('#\n');
f.write('#\n');

# transitions
f.write('# transitions\n');
f.write('T: * : * : * : 0.0\n');
for ja in range(totja):
    jav = decode(ja, actionarity);
    for js in range(totjs):
        jsv = decode(js, statearity);
        nsp = jtransition(jsv, jav, agents);
        for ns, p in nsp.iteritems():
            f.write('T: ' + ja2str(jav, ragents) + ' : ' + vec2str(jsv) + ' : ' 
                    +  vec2str(decode(ns,statearity)) + ' : ' + str(p) + '\n');
f.write('#\n');

# observations
# Right now the observation does not depend on the action, but only on the state
f.write('# observations\n');
f.write('O: * : * : * : 0.0\n');
jav = decode(0, actionarity);
for js in range(totjs):
    jsv = decode(js, statearity);
    nop = jobservation(jav, jsv, ragents, wumpi, rows, cols);
    for no, p in nop.iteritems():
        f.write('O: * : ' + vec2str(jsv) + ' : ' 
                + jo2str(decode(no,obsarity), ragents) + ' : ' + str(p) + '\n');
f.write('#\n');

# rewards
f.write('# rewards\n');
f.write('R: * : * : * : * : 0\n');
for ja in range(totja):
    jav = decode(ja, actionarity);
    for js in range(totjs):
        jsv = decode(js, statearity);
        reward = jreward(jsv, jav, ragents, wumpi, rows, cols);
        if reward != 0:
            f.write('R: ' + ja2str(jav, ragents) + ' : ' + vec2str(jsv) 
                    + ' : * : * : ' + str(reward) + '\n');
f.write('#\n');

f.close();
