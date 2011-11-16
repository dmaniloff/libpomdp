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

# globals
TWIDTH       = 1070
WIDTH        = 900
HEIGHT       = 800
CATCH_REWARD = 10
ROWS         = 5
COLS         = 5
PROBLEM      = '../catch_rect_5_5_rand_adjobs.SPUDD'
ALPHAS       = '../data/catch_rect_rand_adjobs_5_5_rounds1_iter100_nbel10000_nsbel10000_alphas619.alpha'

# declarations
stats            = []
gp               = CatchRectangularGrid(ROWS,COLS)
parser           = dotalphaParserFlat() # the parentheses here are needed!!
parser.parse(ALPHAS)
valueFunction    = parser.getValueFunction()
initState        = [1, 25]              # initial state in factored form - starts from 1 here
pomdpProblem     = pomdpAdd(PROBLEM)    # the idea is that we can have any imlpementation here
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
        self.crewLbl = JLabel("0")
        self.ctrPane.add(self.crewTit)
        self.ctrPane.add(self.crewLbl)
        
        # observation
        self.obsTit = JLabel("Observation")
        self.obsLbl = JLabel("nil")
        self.ctrPane.add(self.obsTit)
        self.ctrPane.add(self.obsLbl)

        # last action
        self.lactTit = JLabel("Last action")
        self.lactLbl = JLabel("nil")
        self.ctrPane.add(self.lactTit)
        self.ctrPane.add(self.lactLbl)

        # about to exec action
        self.nactTit = JLabel("About to exec")
        self.nactLbl = JLabel("nil")
        self.ctrPane.add(self.nactTit)
        self.ctrPane.add(self.nactLbl)

        #######################################################################
        # world panel
        self.wrlPane = JPanel()
        self.wrlPane.setLayout(GridLayout(ROWS,COLS))
        self.wrlPane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT)
        #self.wrlPane.setLayout(None)
        self.wrlPane.setSize(Dimension(WIDTH, HEIGHT))
        self.wrlPane.setBorder(BorderFactory.createTitledBorder(
                                 "World view"))
        self.wrlInsets = self.wrlPane.getInsets()
 
        # agent and wumpus images
        self.wIcon  = ImageIcon('wumpusicon.gif')
        self.wLabel = JLabel(self.scale(self.wIcon.getImage(), 0.6))
        self.aIcon  = ImageIcon('agenticon.gif')
        self.aLabel = JLabel(self.scale(self.aIcon.getImage(), 0.6))

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
        self.cells[apos].add(self.aLabel)
        self.cells[wpos].add(self.wLabel)
        for i in range(len(self.cells)):
            if gp.isLegalPosition(i) == False:
                self.cells[i].setBackground(Color.BLACK)
            self.cells[i].updateUI()
            
    def scale(self, src, scale):
        w = (int)(scale*src.getWidth(None))
        h = (int)(scale*src.getHeight(None))
        type = BufferedImage.TYPE_INT_RGB
        dst = BufferedImage(w, h, type)
        g2 = dst.createGraphics()
        g2.drawImage(src, 0, 0, w, h, Color(.92,.92,.92,0.0), None)
        g2.dispose()
        return ImageIcon(dst)

    def execute_catch(self,event):
        # spawn this on its own thread
        Thread(target=lambda: self.run_sim()).start()

    def run_sim(self):
        episoderunning = True
        
        # starting belief state 
        currbelief = initBelief
        factoredS  = initState
        instance   = 1
        cumrew     = 0
        run        = 1
        # execution loop
        # while(episoderunning):
        while(run <= 100):
            # draw current state
            # self.draw_state(factoredS) 
            
            # extract action from direct controller - ie., 0-step LA
            exreward = valueFunction.V(currbelief)
            action   = valueFunction.directControl(currbelief)
            print "value of b %f and selected action %d" % (exreward,action)

            # show action that we are about to execute
            self.nactLbl.setText(pomdpProblem.getactStr(action))
            time.sleep(1)

            # sample new state, observation, and calculate reward           
            print factoredS
            factoredS1 = pomdpProblem.sampleNextState(factoredS, action)
            print " sampled state is" 
            print factoredS1
            factoredO  = pomdpProblem.sampleObservation(factoredS, factoredS1, action)
            print " sampled o is" 
            print factoredO
            reward = pomdpProblem.getReward(factoredS, action)
            print reward

            # draw new state, and display what's happening
            self.draw_state(factoredS1) 
            self.lactLbl.setText(pomdpProblem.getactStr(action))
            self.nactLbl.setText("nil")
            self.rewLbl.setText (Double.toString(reward))
            cumrew = cumrew + reward*gamma**instance
            self.crewLbl.setText(Double.toString(cumrew))
            self.obsLbl.setText (pomdpProblem.
                                 getobsStr(pomdpProblem.
                                           sencode(factoredO,
                                                   pomdpProblem.getnrObsV(),
                                                   pomdpProblem.getobsArity()) - 1))
            
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
            if self.stepChk.isSelected() == True:
                self.stepBtn.setEnabled(True)
            while self.stepBtn.isEnabled() == True:
                time.sleep(1)
            
            # check whether this episode has ended
            #if reward == CATCH_REWARD - 1:
            #    print "Episode ended!"
            #    episoderunning = false

            # smooth the sim a little in case we're not stepping
            time.sleep(1)

            # stop after 100 iterations
            if instance == 100:
                run = run + 1;
                stats.append(cumrew);
                currbelief = initBelief
                factoredS  = initState
                cumrew = 0;
                instance = 1;

        print stats;

if __name__ == '__main__':
    CatchExecGUI()

