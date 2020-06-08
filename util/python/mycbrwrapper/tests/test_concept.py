from mycbrwrapper.concepts import *
import unittest
from mycbrwrapper.tests.test_base import *

__name__ = "test_concept"

defaulthost = "localhost:8080"

class ConceptTest(CBRTestCase):

    @classmethod
    def setUpClass(cls):
        super(ConceptTest, cls).setUpClass()

    @classmethod
    def tearDownClass(cls):
        super(ConceptTest, cls).tearDownClass()

    def __init__(self, *args, **kwargs):
        super(ConceptTest, self).__init__(*args, **kwargs)

    def test_create_and_delete_concept(self):
        c = Concepts(defaulthost)
        conceptstring = "test_concept_test1"
        c.addConcept(conceptstring)
        c.deleteConcept(conceptstring)
