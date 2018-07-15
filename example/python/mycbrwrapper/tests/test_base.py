from mycbrwrapper.rest import getRequest
import unittest

__name__ = "test_base"

defaulthost = "localhost:8080"
"""
The model of the case base for the unit tests are simple
id,name,doubleattr1,doubleattr2
"""


class CBRTestCase(unittest.TestCase):
    casesJSON = """{"cases" : [
    {
    "wind_speed": "0",
    "wind_from_direction": "0",
    "wind_effect": "0"
    },
    {
    "wind_speed": "5.2",
    "wind_from_direction": "279",
    "wind_effect": "5.3"
    },
    {
    "wind_speed": "2.1",
    "wind_from_direction": "339",
    "wind_effect": "1.05"
    }
    ]}"""
    localSimID = "testLocalSimilarityFunction"
    localSimJSON = """{{
    "id"="{}"
    "type"="Double"
    "subtype"="Polywidth"
    "parameters"="4.5"
    }}""".format(localSimID)

    amalgamationSimID = "testAmalgmamationSimilarityFunction1"

    # here type can be either of MINIMUM, MAXIMUM, WEIGHTED_SUM, EUCLIDEAN, NEURAL_NETWORK_SOLUTION_DIRECTLY,SIM_DEF;
    

    def __init__(self, *args, **kwargs):
        super(CBRTestCase, self).__init__(*args, **kwargs)
    @classmethod
    def getAttributeParamterJSON(cls,min,max):
        return """
        {{
        "type": "Double",
        "min": "{}",
        "max": "{}"
        }}
        """.format(min,max)

    @classmethod
    def setUpClass(cls):
        print("in super setupclass")
        cls.createTestCaseBase()
        cls.createConcept()
        cls.createAttributes()
        cls.createLocalSimilarityFunctions()
        cls.createAmalgamationFunctions()
        cls.createCases()

    @classmethod
    def tearDownClass(cls):
        print("in super teardownclass")
        cls.destroyCases()
        cls.destroyLocalSimilarityFunctions()
        cls.destroyAmalgamationFunctions()
        cls.destroyAttributes()
        cls.destroyConcept()
        cls.destroyTestCaseBase()

    @classmethod
    def createTestCaseBase(cls, host=defaulthost):
        print("in createTestCaseBase")
        api = getRequest(host)
        call = api.casebases
        result = call.PUT("unittestCB")
        print("url : {}".format(call._url))
        print("result : {}".format(result))

    @classmethod
    def createConcept(cls, host=defaulthost):
        """
        This is now working, it creates a concept.
        """
        print("in createconcept")
        api = getRequest(host)
        call = api.concepts
        result = call.PUT("testconcept")
        print("url : {}".format(call._url))
        print("result : {}".format(result))

    @classmethod
    def createAttributes(cls, host=defaulthost):
        api = getRequest(host)
        api.concepts("testconcept").attributes\
            .PUT("wind_speed",params={"attributeJSON":cls.getAttributeParamterJSON(0,25)})
        api.concepts("testconcept").attributes\
            .PUT("wind_from_direction",params={"attributeJSON":cls.getAttributeParamterJSON(0,361)})
        api.concepts("testconcept").attributes\
            .PUT("wind_effect",params={"attributeJSON":cls.getAttributeParamterJSON(0,40)})

    @classmethod
    def createLocalSimilarityFunctions(cls, host=defaulthost):
        api = getRequest(host)
        api.concepts("testconcept").attributes("wind_speed")\
                    .similarityfunctions\
                    .PUT(cls.localSimJSON)

    @classmethod
    def createAmalgamationFunctions(cls, host=defaulthost):
        api = getRequest(host)
        call = api.concepts("testconcept").amalgamationFunctions(cls.amalgamationSimID)

        result = call.PUT(params={"amalgamationFunctionType":"NEURAL_NETWORK_SOLUTION_DIRECTLY"})
        print("add alg url {} result {}".format(call._url,result))

    @classmethod
    def createCases(cls, host=defaulthost):
        print("in createcases")
        api = getRequest(host)
        call = api.concepts("testconcept").casebases("unittestCB").instances
        call.PUT(params={'cases':cls.casesJSON})

        print("url: {} ".format(call._url))

    @classmethod
    def destroyTestCaseBase(cls, host=defaulthost):
        api = getRequest(host)
        api.casebases("unittestCB").DELETE()

    @classmethod
    def destroyConcept(cls, host=defaulthost):
        api = getRequest(host)
        call = api.concepts("testconcept")
        call.DELETE()
        print("in delete concept url is: {}".format(call._url))

    @classmethod
    def destroyAttributes(cls, host=defaulthost):
        api = getRequest(host)
        api.concepts("testconcept").attributes("wind_speed").DELETE()
        api.concepts("testconcept").attributes("wind_from_direction").DELETE()
        api.concepts("testconcept").attributes("wind_effect").DELETE()

    @classmethod
    def destroyLocalSimilarityFunctions(cls, host=defaulthost):
        api = getRequest(host)
        api.concepts("testconcept").attributes("wind_speed")\
                                   .similarityfunction(cls.localSimID)\
                                   .DELETE()

    @classmethod
    def destroyAmalgamationFunctions(cls, host=defaulthost):
        api = getRequest(host)
        api.concepts("testconcept")\
            .amalgamationFunctions(cls.amalgamationSimID).DELETE()

    @classmethod
    def destroyCases(cls, host=defaulthost):
        api = getRequest(host)
        api.concepts("testconcept").casebases("unittestCB").instances.DELETE()

if __name__ == "__main__":
    unittest.main()
