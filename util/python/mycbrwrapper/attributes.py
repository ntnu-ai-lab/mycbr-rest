from mycbrwrapper.rest import getRequest

__name__ = "attributes"

class Attribute():

    def __init__(self, host, name, concept, attribute_parameters, get=False):
        self.host = host
        self.name = name
        self.concept = concept
        self.attribute_parameters = attribute_parameters
        self.similarityfunctions = []
        if get is True:
            self.getRemoteAttribute()
        else:
            self.createAttribute(name)

    def createAttribute(self, name):
        api = getRequest(self.host)
        #print(f"in create attritube name: {name} and attribute parameters: {self.attribute_parameters}")
        result = api.concepts(self.concept.name).attributes.PUT(self.name,params={"attributeJSON":self.attribute_parameters})
        #print(result)

    def getRemoteAttribute(self):
        api = getRequest(self.host)
        call = api.concepts(self.concept.name).attributes(self.name)
        result = call.GET()
        resultJson = result.json()
        self.attType = resultJson.get("type")
        if "Double" in self.attType:
            self.minvalue = resultJson.get("min")
            self.maxvalue = resultJson.get("max")
            print("created a double attribute with min: {} and max: {}"
                  .format(self.minvalue,self.maxvalue))
        #concepts = results.json().get("concept")
        
