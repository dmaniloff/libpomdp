# /** ------------------------------------------------------------------------- *
#  * libpomdp
#  * ========
#  * File: CatchExecGUI.py
#  * Description: 
#  * Copyright (c) 2009, 2010 Diego Maniloff 
#  * W3: http://www.cs.uic.edu/~dmanilof
#  --------------------------------------------------------------------------- */


# imports
import sys
sys.path.append('../../general/java')
sys.path.append('../../external/symPerseusJava')
sys.path.append('../../external/antlr-3.2.jar')
sys.path.append('../../external/jmatharray.jar')
sys.path.append('../../general/java/alpha-parser')
from org.antlr.runtime import *
from org.math.array import *
from java.lang import Integer
from java.lang import Double
from javax.swing import *
from java.awt import *
from threading import Thread
import time
import pomdpAdd
import CatchGridProperties
import CatchRectangularGrid
import dotalphaParserFlat
import AbstractMap
import Config
import DD
import DDcollection
import DDleaf
import DDnode
import Global
import HashMap
import LinkedHashMap
import MyDoubleArray
import MySet
import OP
import Pair
import ParseSPUDD
import StreamTokenizer
import TripletConfig
import TripletSet


# globals
TWIDTH       = 900
WIDTH        = 700
HEIGHT       = 700
CATCH_REWARD = 10
ROWS         = 5
COLS         = 5
PROBLEM      = 'catch_rect_5_5.SPUDD'
ALPHAS       = 'symperseus-log/catch_value_function.alpha'

# declarations
gp               = CatchRectangularGrid(ROWS,COLS)
parser           = dotalphaParserFlat() # the parentheses here are needed!!
parser.parse(ALPHAS)
valueFunction    = parser.getValueFunction()
initState        = [1, ROWS * COLS ] # initial state in factored form - starts counting from 1 here
pomdpProblem     = pomdpAdd(PROBLEM) # the idea is that we can have any imlpementation here
currState        = initState
currBelief       = pomdpProblem.getInit()

# figure out all possible initial states of the pomdp
# in the future, this little routine should be pushed into the pomdp object
states           = pomdpProblem.getListofInitStates()

# useful objects
wIcon  = ImageIcon('wumpusicon.gif')
wLabel = JLabel(wIcon)
aIcon  = ImageIcon('agenticon.gif')
aLabel = JLabel(aIcon)


# main class
class CatchExecGUI:
    #######################################################################
    # initialization
    def __init__(self):
        # main window container
        self.frame = JFrame('Catch problem simulator',
                            # Exit the application, using System.exit(0)
                            defaultCloseOperation = JFrame.EXIT_ON_CLOSE                                 
                            )
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
        self.startBtn = JButton('START', actionPerformed=self.execute_catch)
        self.ctrPane.add(self.startBtn)

        # stop button
        self.stopBtn  = JButton('STOP', actionPerformed=self.exit)
        self.ctrPane.add(self.stopBtn)

        # step selector
        self.stepChk  = JCheckBox("Step", False) # , actionPerformed=self.chk_step)
        self.ctrPane.add(self.stepChk)

        # step button
        self.stepBtn  = JButton('STEP', actionPerformed=self.step)
        self.stepBtn.setEnabled(False)
        self.ctrPane.add(self.stepBtn)

        # immediate reward received
        self.rewTit = JLabel("Reward")
        self.rewLbl = JLabel("nil")
        self.ctrPane.add(self.rewTit)
        self.ctrPane.add(self.rewLbl)

        # cummulative reward
        self.crewTit = JLabel("Cummulative")
        self.crewLbl = JLabel("nil")
        self.ctrPane.add(self.crewTit)
        self.ctrPane.add(self.crewLbl)
        
        # observation
        self.obsTit = JLabel("Observation")
        self.obsLbl = JLabel("nil")
        self.ctrPane.add(self.obsTit)
        self.ctrPane.add(self.obsLbl)

        # action
        self.actTit = JLabel("Action")
        self.actLbl = JLabel("nil")
        self.ctrPane.add(self.actTit)
        self.ctrPane.add(self.actLbl)

        #######################################################################
        # world panel
        self.wrlPane = JPanel()
        self.wrlPane.setLayout(GridLayout(ROWS,COLS))
        self.wrlPane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT)
        #self.wrlPane.setLayout(None)
        self.wrlPane.setPreferredSize(Dimension(WIDTH, HEIGHT))
        self.wrlPane.setBorder(BorderFactory.createTitledBorder(
                                 "World view"))
        self.wrlInsets = self.wrlPane.getInsets()
 
        # grid cells
        self.cells   = [] # make sure this is OK
        for i in range(ROWS*COLS): 
            self.cells.append(JPanel())            
            self.cells[i].setBorder(BorderFactory.createLineBorder(Color.gray))
            self.wrlPane.add(self.cells[i])

        #######################################################################
        # add panels to main panel
        self.panel.add(self.ctrPane)
        self.panel.add(self.wrlPane)
        # main panel to frame
        self.frame.add(self.panel)
        # display GUI
        self.draw_state(initState)        
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
       #  if self.stepChk.isSelected() == False:
#             print "is false"
#             self.stepChk.setSelected(True)
#         elif self.stepChk.isSelected() == True:
#             print "is true"
#             self.stepChk.setSelected(False)
#         if self.stepChk.isSelected() == True:
#             self.stepChk.setSelected(False)
#         else:
#             self.stepChk.setSelected(True)

    def step(self, event):
        self.stepBtn.setEnabled(False)
    
    def exit(self, event):
        exit(0)

    def change_text(self, event):
        print 'Clicked!'

    def draw_state(self, state):
        for i in range(len(self.cells)):
            self.cells[i].removeAll()        
        apos = ROWS * COLS  - state[0]
        wpos = ROWS * COLS  - state[1]
        self.cells[apos].add(aLabel)
        self.cells[wpos].add(wLabel)
        for i in range(len(self.cells)):
            self.cells[i].updateUI()

    def execute_catch(self,event):
        # spawn this on its own thread
        Thread(target=lambda: self.run_sim()).start()

    def run_sim(self):
        episoderunning = True
        
        # starting belief state and state from where we left off
        currbelief = currBelief
        factoredS  = currState

        # execution loop
        while(episoderunning):

            # extract action from direct controller - ie., 0-step LA
            exreward = valueFunction.V(currbelief)
            action   = valueFunction.directControl(currbelief)
            print "value of b %f and selected action %d" % (exreward,action)

            # execute action, percieve observation and receive reward           
            print factoredS
            factoredS1 = pomdpProblem.sampleNextState(factoredS, action)
            print factoredS1
            factoredO  = pomdpProblem.sampleObservation(factoredS, factoredS1, action)
            print factoredO
            
            reward = pomdpProblem.getReward(factoredS, action)
            print reward

            # display some info
            self.actLbl.setText(pomdpProblem.getactStr(action)) 
            self.rewLbl.setText(Double.toString(reward))
            self.obsLbl.setText(pomdpProblem.
                                getobsStr(IntegerArray.product(factoredO) - 1))

            # draw new state
            self.draw_state(factoredS) # or S1?
            self.panel.repaint() # try this
            self.wrlPane.repaint()
            self.ctrPane.repaint()

            # iterate
            nextbelief = pomdpProblem.tao(currbelief, action, IntegerArray.product(factoredO) - 1)
            currbelief = nextbelief;
            factoredS  = factoredS1;

            # step button
            while self.stepBtn.isEnabled() == True:
                time.sleep(1)
            if self.stepChk.isSelected() == True:
                self.stepBtn.setEnabled(True)

            # check whether this episode has ended
            if reward == CATCH_REWARD:
                print "Episode ended!"
                episoderunning = false

            # smooth the sim a little in case we're not stepping
            time.sleep(1)


if __name__ == '__main__':
    CatchExecGUI()

