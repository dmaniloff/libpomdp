# /** ------------------------------------------------------------------------- *
#  * libpomdp
#  * ========
#  * File: CatchExecGUI_Multiagent.py
#  * Description: multiagent version, makes sense with a random wumpus only
#                 uses a threaded approach to run the pomdp policies in parallel
#  * Copyright (c) 2009, 2010 Diego Maniloff 
#  * W3: http://www.cs.uic.edu/~dmanilof
#  --------------------------------------------------------------------------- */


# imports
import sys
sys.path.append('../../../../../external/symPerseusJava.jar')
sys.path.append('../../../../../external/antlr-3.2.jar')
sys.path.append('../../../../../external/jmatharray.jar')
sys.path.append('../../../../../external/mtj-0.9.12.jar')
sys.path.append('../../../../../external/ujmp-complete-0.2.4.jar')
sys.path.append('../../../../../dist/libpomdp.jar')
from libpomdp.general.java import *
from libpomdp.problems.catchproblem import *
from symPerseusJava import *
from org.antlr.runtime import *
from org.math.array import *
from java.lang import Integer
from java.lang import Double
from javax.swing import *
from java.awt import *
from java.awt.image import *
from threading import Thread
import time
# additional threading imports
from Queue import Queue
import thread

# globals
TWIDTH       = 1070
WIDTH        = 900
HEIGHT       = 800
CATCH_REWARD = 10
ROWS         = 5
COLS         = 5
PROBLEM      = '../catch_rect_5_5_rand_adjobs.SPUDD'
ALPHAS       = '../data/catch_rect_rand_adjobs_5_5_rounds1_iter100_nbel10000_nsbel10000_alphas619.alpha'
NUM_AGENTS   = 2

# declarations
gp               = CatchRectangularGrid(ROWS,COLS)
parser           = dotalphaParserFlat() # the parentheses here are needed!!
parser.parse(ALPHAS)
valueFunction    = parser.getValueFunction()
# initial state in factored form - starts from 1 here
initState        = [1, 25, 13]
# the idea is that we can have any imlpementation here
pomdpProblem     = pomdpAdd(PROBLEM) 
initBelief       = pomdpProblem.getInit()
gamma            = pomdpProblem.getGamma()
# figure out all possible initial states of the pomdp
states           = pomdpProblem.getListofInitStates()


# main class
class CatchExecGUI:
    #######################################################################
    # initialization
    def __init__(self):
        # main window container
        self.frame = JFrame('Catch problem simulator',
                            # Exit the application, using System.exit(0)
                            defaultCloseOperation = JFrame.EXIT_ON_CLOSE)
        self.panel = JPanel()
        self.panel.setLayout(BoxLayout(self.panel, BoxLayout.LINE_AXIS))
        self.panel.setPreferredSize(Dimension(WIDTH, HEIGHT))

        #######################################################################
        # control panel
        self.ctrPane = JPanel()
        self.ctrPane.setPreferredSize(Dimension(TWIDTH - WIDTH, HEIGHT))
        self.ctrPane.setLayout(BoxLayout(self.ctrPane, BoxLayout.PAGE_AXIS))
        self.ctrPane.setBorder(BorderFactory.createTitledBorder(
                                 "Controls and Info"));

        # start button
        self.startBtn = JButton('START', actionPerformed=self.start_pressed)
        self.ctrPane.add(self.startBtn)

        # stop button
        self.stopBtn  = JButton('STOP', actionPerformed=self.exit)
        self.ctrPane.add(self.stopBtn)

        # step selector
        # self.stepChk  = JCheckBox("Step", False) # , actionPerformed=self.chk_step)
#         self.ctrPane.add(self.stepChk)

        # step button
       #  self.stepBtn  = JButton('STEP', actionPerformed=self.step)
#         self.stepBtn.setEnabled(False)
#         self.ctrPane.add(self.stepBtn)

        # immediate reward received
        self.rewTit = JLabel("Reward")
        self.rewLbl = JLabel("nil")
        self.ctrPane.add(self.rewTit)
        self.ctrPane.add(self.rewLbl)

        # cummulative reward
        self.crewTit = JLabel("Cummulative")
        self.crewLbl = JLabel("0")
        self.ctrPane.add(self.crewTit)
        self.ctrPane.add(self.crewLbl)
        
        # observation
        # self.obsTit = JLabel("Observation")
#         self.obsLbl = JLabel("nil")
#         self.ctrPane.add(self.obsTit)
#         self.ctrPane.add(self.obsLbl)

        # last action
        # self.lactTit = JLabel("Last action")
#         self.lactLbl = JLabel("nil")
#         self.ctrPane.add(self.lactTit)
#         self.ctrPane.add(self.lactLbl)

        # about to exec action
        # self.nactTit = JLabel("About to exec")
#         self.nactLbl = JLabel("nil")
#         self.ctrPane.add(self.nactTit)
#         self.ctrPane.add(self.nactLbl)

        #######################################################################
        # world panel
        self.wrlPane = JPanel()
        self.wrlPane.setLayout(GridLayout(ROWS,COLS))
        self.wrlPane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT)
        self.wrlPane.setSize(Dimension(WIDTH, HEIGHT))
        self.wrlPane.setBorder(BorderFactory.createTitledBorder(
                                 "World view"))
        self.wrlInsets = self.wrlPane.getInsets()
 
        # agents and wumpus images
        self.wIcon  = ImageIcon('wumpusicon.gif')
        # self.wLabel = JLabel(self.scale(self.wIcon.getImage(), 0.6))
        self.aIcon  = ImageIcon('agenticon.gif')
        
        # one aLabel per agent
        # self.aLabel = [JLabel(self.scale(self.aIcon.getImage(), 0.6))] * NUM_AGENTS 
        # self.aLabel = JLabel(self.scale(self.aIcon.getImage(), 0.6))

        # grid cells
        self.cells   = [] # make sure this is OK
        for i in range(ROWS*COLS): 
            self.cells.append(JPanel())            
            #self.cells[i].setSize(Dimension(WIDTH/COLS, HEIGHT/ROWS)) 
            self.cells[i].setBorder(BorderFactory.createLineBorder(Color.gray))
            self.wrlPane.add(self.cells[i])

        #######################################################################
        # add panels to main panel
        self.panel.add(self.ctrPane)
        self.panel.add(self.wrlPane)
        # main panel to frame
        self.frame.add(self.panel)
        # display GUI
        self.draw_state(initState, 0, 0)        
        self.show()

    #######################################################################
    # methods
    def show(self):
        # The pack method sizes the frame so that all its contents are
        # at or above their preferred sizes.
        self.frame.pack()
        self.frame.visible = True

    def chk_step(self, event):
        print "s"

    def step(self, event):
        self.stepBtn.setEnabled(False)
    
    def exit(self, event):
        exit(0)

    def change_text(self, event):
        print 'Clicked!'

    '''
    gather_jstate:
    retrieve info from all agents
    to compose a joint state that can be drawn
    by draw_state
    it could happen that a thread calls gather_jstate twice before
    another was able to call it at all - for this we need syncing here
    '''
    # def gather_jstate(self, agentpos, agentid):                        
#         global reported
#         global wpos
#         global state
#         reported = reported + 1
#         state[agentid] = agentpos
#         # last agent reported
#         if reported == NUM_AGENTS:
#             reported = 0
#             # get the next state of the RANDOM wumpus
#             # actions start from zero, but the state from 1
#             wpos = pomdpProblem.sampleNextState([1, wpos], 0)[1]
#             state[NUM_AGENTS] = wpos
#             # draw joint state
#             self.draw_state(state)

    '''
    draw_state:
    draw a joint state that contains NUM_AGENTS agents
    and a single wumpus
    state is a vector of length NUM_AGENTS + 1, whose first
    NUM_AGENTS elements are the agents' positions and last element
    the wumpus' position
    '''
    def draw_state(self, state, treward, creward):
        # declarations
        apos = [None] * NUM_AGENTS
        # clear cells
        for i in range(len(self.cells)):
            self.cells[i].removeAll()        
        # get agents' positions
        for i in range(NUM_AGENTS):
            apos[i] = ROWS * COLS  - state[i]
        # last element in the array is wumpus' position
        wpos = ROWS * COLS  - state[NUM_AGENTS]
        # add agents
        for i in range(NUM_AGENTS):
            self.cells[apos[i]].add(JLabel(self.scale(self.aIcon.getImage(), 0.6)))
        # add the wumpus
        self.cells[wpos].add(JLabel(self.scale(self.wIcon.getImage(), 0.6)))
        # gray-out illegal positions and update all cells
        for i in range(len(self.cells)):
            if gp.isLegalPosition(i) == False:
                self.cells[i].setBackground(Color.BLACK)
            self.cells[i].updateUI()
        # display total and cum rew on ctrl panel
        self.rewLbl.setText(Double.toString(treward))
        self.crewLbl.setText(Double.toString(creward))
            
    def scale(self, src, scale):
        w = (int)(scale*src.getWidth(None))
        h = (int)(scale*src.getHeight(None))
        type = BufferedImage.TYPE_INT_RGB
        dst = BufferedImage(w, h, type)
        g2 = dst.createGraphics()
        g2.drawImage(src, 0, 0, w, h, Color(.92,.92,.92,0.0), None)
        g2.dispose()
        return ImageIcon(dst)


    '''
    START button pressed
    '''
    def start_pressed(self, event):
        thread.start_new_thread(self.execute_catch, (None,))
    
    '''
    Main thread, starts when the START button is pressed
    '''
    def execute_catch(self, dummy):
        # declarations
        global initState
        global gamma
        instance = 1
        wpos   = initState[NUM_AGENTS]
        jstate = [None] * (NUM_AGENTS + 1)
        jrewrd = [None] * (NUM_AGENTS)
        state_rew_queue = Queue(NUM_AGENTS)
        wumpus_queue    = Queue(NUM_AGENTS)
        cumrew          = 0

        # spawn one thread for each agent
        for i in range(NUM_AGENTS):
            # Thread(target=lambda: self.run_sim(i)).start()
            thread.start_new_thread(self.run_agent, (i, state_rew_queue, wumpus_queue,))
        
        # the main thread will consume all elements from the queue, and once
        # empty will allow for execution to continue on each of the agent threads
        while True:   
            # first, the wumpus moves - sample this from the model, as it is random
            # it doesn't matter where the agent might be, or its action
            wpos_new = pomdpProblem.sampleNextState([1, wpos], 0)[1]
            # tell all agent threads where the wumpus is so they can produce an obs
            for i in range(NUM_AGENTS):
                wumpus_queue.put(wpos_new)
            # now wait for the state queue to fill
            while not state_rew_queue.full():
                pass
            # remove all items and assemble a state vector for drawer
            for i in range(NUM_AGENTS):
                item     = state_rew_queue.get() 
                jstate[item[0]] = item[1]
                jrewrd[item[0]] = item[2]
                state_rew_queue.task_done()
                print "MAIN: gathered item %d from state_rew_queue" % (i)
            # at this point all agent threads that called join will resume                
            jstate[NUM_AGENTS] = wpos_new
            # iterate wpos
            wpos = wpos_new
            # calculate total cumulative reward for this instance
            treward = 0
            for i in range(NUM_AGENTS):
                treward = treward + jrewrd[i]
            cumrew = cumrew + treward*gamma**instance
            # draw joint state
            self.draw_state(jstate, treward, cumrew)
            time.sleep(2)
            # iterate
            instance = instance + 1
                
                
    '''
    agents thread function
    '''
    def run_agent(self, agentid, state_rew_queue, wumpus_queue):
        episoderunning = True
        
        # starting local belief state, and simulation local state
        currbelief = initBelief
        factoredS  = [initState[agentid], initState[NUM_AGENTS]]
        instance   = 1
        cumrew     = 0

        # execution loop
        while(episoderunning):
            
            # extract action from direct controller - ie., 0-step LA
            exreward = valueFunction.V(currbelief)
            action   = valueFunction.directControl(currbelief)
            print "THREAD %d: value of b %f and selected action %d" % (agentid,
                                                                       exreward,
                                                                       action)
            # show action that we are about to execute
            # self.nactLbl.setText(pomdpProblem.getactStr(action))
#             time.sleep(1)

            # sample new local state           
            print factoredS
            factoredS1 = pomdpProblem.sampleNextState(factoredS, action)
            print "THREAD %d: sampled local apos' is %s" % (agentid, str(factoredS1[0]))

            # obtain position of the common wumpus and update local state
            factoredS1[1] = wumpus_queue.get()
            wumpus_queue.task_done()
            wumpus_queue.join()

            # can now sample observation locally, it will jibe with the common sim
            factoredO  = pomdpProblem.sampleObservation(factoredS, factoredS1, action)
            print "THREAD %d: sampled local obs is %s\n" % (agentid, str(factoredO))

            # can now also obtain local reward
            reward = pomdpProblem.getReward(factoredS, action)
            print "THREAD %d: reward collected is %d" % (agentid, reward)

            # send new state and reward info to collector, this should be a sync'ed call
            # we only send this agent's position in the message and the reward
            # wait for all positions from all agent threads to be read in by main
            state_rew_queue.put([agentid, factoredS1[0], reward])
            state_rew_queue.join() 

            # print info on the info pane (prob need to delete this)
            # self.lactLbl.setText(pomdpProblem.getactStr(action))
#             self.nactLbl.setText("nil")
#             self.rewLbl.setText (Double.toString(reward))
#             cumrew = cumrew + reward*gamma**instance
#             self.crewLbl.setText(Double.toString(cumrew))
#             self.obsLbl.setText (pomdpProblem.
#                                  getobsStr(pomdpProblem.
#                                            sencode(factoredO,
#                                                    pomdpProblem.getnrObsV(),
#                                                    pomdpProblem.getobsArity()) - 1))
            
            # iterate
            nextbelief = pomdpProblem.tao(currbelief,
                                          action,
                                          pomdpProblem.
                                          sencode(factoredO,
                                                  pomdpProblem.getnrObsV(),
                                                  pomdpProblem.getobsArity()) - 1)
            currbelief = nextbelief;
            factoredS  = factoredS1;
            instance   = instance + 1;

            # step button
            # if self.stepChk.isSelected() == True:
#                 self.stepBtn.setEnabled(True)
#             while self.stepBtn.isEnabled() == True:
#                 time.sleep(1)
            
            # check whether this episode has ended
            #if reward == CATCH_REWARD - 1:
            #    print "Episode ended!"
            #    episoderunning = false

            # smooth the sim a little in case we're not stepping
            # time.sleep(1)


if __name__ == '__main__':
    CatchExecGUI()

