from mycbrwrapper.concepts import Concepts
import unittest
from mycbrwrapper.tests.test_base import CBRTestCase
defaulthost = "localhost:8080"

__name__ = "test_amalgamationfunctions"

class NeuralAmalgmationTest(CBRTestCase):

    @classmethod
    def setUpClass(cls):
        super(NeuralAmalgmationTest, cls).setUpClass()

    @classmethod
    def tearDownClass(cls):
        super(NeuralAmalgmationTest, cls).tearDownClass()

    def __init__(self, *args, **kwargs):
        super(NeuralAmalgmationTest, self).__init__(*args, **kwargs)

    def test_create_and_delete_neural_amalgamation_function(self):
        print("in test")
        cs = Concepts(defaulthost)
        conceptstring = "test_concept_test1"
        amalstring = "neuralamal"
        filesDict = {"h5": "/home/epic/research/dataGetters/balanced_operationalmodel.h5",
                     "json": "/home/epic/research/dataGetters/balanced_operationalmodel.json"}
        c = cs.addConcept(conceptstring)
        c.addNeuralAmalgamationFunction(amalstring,filesDict)
        #c.addNeuralAmalgamationFunction(amalstring)
        cs.deleteConcept(conceptstring)

if __name__ == "__main__":
    unittest.main()
