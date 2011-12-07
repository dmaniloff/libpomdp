"""
Compute bounds for a given pomdp problem.
"""

# imports
import sys
sys.path.append('../../../dist/libpomdp.jar')
sys.path.append('../../../external/antlr-3.2.jar')
sys.path.append('../../../external/mtj-0.9.12.jar')
sys.path.append('../../../external/jmatharray.jar')

from os.path import basename
from optparse import OptionParser
from fnmatch import fnmatch

from java.io import ObjectOutputStream
from java.io import FileOutputStream

from libpomdp.parser import FileParser
from libpomdp.solve.offline.bounds import BpviAdd
from libpomdp.solve.offline.bounds import BpviStd
from libpomdp.solve.offline.bounds import QmdpAdd
from libpomdp.solve.offline.bounds import QmdpStd

# arguments
parser = OptionParser()
parser.add_option("-p", "--pomdp",
                  dest="filename",
                  metavar="POMDP_FILE",
                  help="pomdp filename to compute bounds for")
(options, args) = parser.parse_args()

if not options.filename:
    parser.error("Must provide pomdp filename")

# figure out representation
flat_rep = fnmatch(options.filename, '*.POMDP')
rep = FileParser.PARSE_CASSANDRA_POMDP if flat_rep else \
    FileParser.PARSE_SPUDD

# load problem
pomdp = FileParser.loadPomdp(options.filename, rep)

# lower bound
bp_calc = BpviStd(pomdp) if flat_rep else BpviAdd(pomdp)
lbound = bp_calc.getValueFunction()

# upper bound
qp_calc = QmdpStd(pomdp) if flat_rep else QmdpAdd(pomdp)
ubound = qp_calc.getValueFunction()

# serialize
usuffix = '.QMDP_STD.ser' if flat_rep else '.QMDP_ADD.ser'
ufos = FileOutputStream( basename(options.filename) + usuffix )
uout = ObjectOutputStream(ufos)
uout.writeObject(ubound)
uout.close()

lsuffix = '.BLIND_STD.ser' if flat_rep else '.BLIND_ADD.ser'
lfos = FileOutputStream( basename(options.filename) + lsuffix )
lout = ObjectOutputStream(lfos)
lout.writeObject(lbound)
lout.close()
