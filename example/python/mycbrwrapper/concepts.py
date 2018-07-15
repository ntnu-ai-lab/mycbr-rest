from mycbrwrapper.rest import getRequest
from mycbrwrapper.attributes import Attribute
from mycbrwrapper.amalgamationfunctions import *
from mycbrwrapper.instances import Instance,Instances
from mycbrwrapper.casebases import CaseBase

__name__ = "concepts"

class Concept():

    def init(self, host):
        self.attributes = {}
        self.amalgamationFunctions = {}
        self.casebases = {}
        self.created = False
        self.host = host
        self.instances = Instances(self,host)

    def __init__(self, host, name, get=False):
        self.init(host)
        self.name = name
        if get is True:
            self.getRemoteConcept()
        else:
            self.createConcept(name)

    def getRemoteConcept(self):
        api = getRequest(self.host)
        #not needed as concept has no metainfo other than name
        getConceptCall = api.concepts(self.name)
        #this is needed though
        getAttributesCall = api.concepts(self.name).attributes
        attributesresults = getAttributesCall.GET()
        attributes = attributesresults.json().get("attributes")
        for elem in attributes:
            attributes.append(Attribute(self.host, elem["name"], self))

    def createConcept(self, name):
        """
        This is now working, it creates a concept.
        """
        api = getRequest(self.host)
        call = api.concepts
        result = call.PUT(name)
        self.created = True

    def addAttribute(self, name, attribute_parameters):
        a = Attribute(self.host, name, self, attribute_parameters )
        self.attributes[name]=a
        return a

    def addCaseBase(self, name):
        cb = CaseBase(self,name,self.host)
        self.casebases[name] = cb

 
    def addInstance(self, name, instance_parameters, casebase):
        i = self.instances.addInstance(self, name, self.host, casebase, instance_parameters)

    def addInstances(self, instance_parameters, casebase):
        i = self.instances.addInstances(instance_parameters, casebase)

    def addAmalgamationFunction(self, name, parameters):
        af = AmalgamationFunction(self.host, name, parameters, self)
        self.amalgamationFunctions[name]= af
        return af

    def addNeuralAmalgamationFunction(self, name, files):
        af = NeuralAmalgamationFunction(self.host, name, files, self)
        self.amalgamationFunctions[name]= af
        return af

    def delete(self):
        #TODO remove casebases too..
        for key,value in self.casebases.items():
            self.instances.deleteInstances(key)
        self.attributes.clear()
        self.amalgamationFunctions.clear()
        # for key in self.attributes:
        #     self.attributes[key].delete()
        #     self.attributes.pop(key)
        # for key in self.amalgamationFunctions:
        #     self.amalgamationFunctions[key].delete()
        #     self.amalgamationFunctions.pop(key)

    def caseBaseList(self):
        return list(self.casebases.values())

    def instanceList(self):
        return self.instances.instanceList()


class Concepts():

    def __init__(self, host, get=False):
        self.concepts = {}
        self.host = host
        if get is True:
            self.getRemoteConcepts()

    def addConcept(self, name):
        c = Concept(self.host, name)
        self.concepts[name] = c
        return c

    def deleteConcept(self, name):
        concept = None
        if name in self.concepts:
            concept = self.concepts[name]
            self.concepts.pop(name) 
        else:
            return None
        concept.delete()
        api = getRequest(self.host)
        call = api.concepts(concept.name)
        result = call.DELETE()

    def deleteAllConcepts(self):
        for conceptname in self.concepts:
            self.deleteConcept(conceptname)

    def __iter__(self):
        self.counter = 0
        return self

    def __next__(self):  # Python 3: def __next__(self)
        if self.counter+1 == len(self.concepts):
            raise StopIteration
        else:
            self.counter += 1
            return self.concepts[self.counter - 1]

    def getRemoteConcepts(self):
        api = getRequest(self.host)
        call = api.concepts
        result = call.GET()
        concepts = result.json().get("concept")
        for elem in concepts:
            concepts.append(Concept(self.host,elem,get=True))
