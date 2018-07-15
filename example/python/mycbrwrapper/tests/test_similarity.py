from mycbrwrapper.tests.test_base import *

__name__ = "test_similarity"

class SimilarityTests(CBRTestCase):
    @classmethod
    def setUpClass(cls):
        super(SimilarityTests, cls).setUpClass()

    @classmethod
    def tearDownClass(cls):
        super(SimilarityTests, cls).tearDownClass()

    def test_getSimilarityFunction(self):
        api = getRequest(defaulthost)
        call = api.concepts("testconcept").attributes("wind_speed").similarityfunctions
        result = call.GET()
        print(result.json())

    def test_getAllCases(self):
        api = getRequest(defaulthost)
        call = api.concepts("testconcept").instances
        results = call.GET()
        print("url: {} len of elems.. {}".format(call._url,len(results.json())))


    def test_retrieval(self):
        api = getRequest(defaulthost)
        call = api.concepts("testconcept").casebases("unittestCB").retrievalByID
        result = call.GET(params={
            "caseID":"testconcept1"
        })
        print(result.json())

    def __init__(self, *args, **kwargs):
        super(SimilarityTests, self).__init__(*args, **kwargs)

