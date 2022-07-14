from mycbrwrapper.rest import *
import json

__name__ = "instances"

class Instance():
    def __init__(self, concept, instanceid, host, casebase, instance_parameters, get=False):
        self.instanceid = instanceid
        self.casebase = casebase
        self.concept = concept
        self.host = host
        if get is False:
            self.createInstance(instance_parameters)
        self.instance_parameters=instance_parameters
        

    def createInstance(self, instance_parameters):
        api = getRequest(self.host)
        result = api.concepts(self.concept.name).casebases(self.casebase).instances(self.instanceid).PUT(params={'casedata':instance_parameters})

class Instances():
    def __init__(self, concept, host):
        self.concept = concept
        self.host = host
        self.instances = {}

    def addInstance(self, instanceid, instance_parameters,casebase):
        if instanceid in self.instances:
            return
        i = Instance(self.concept,instanceid,self.host,casebase,instance_parameters)
        self.instances[instanceid] = i
        return i

    def addInstances(self, case_data_json, casebase):
        api = getRequest(self.host)
        result = api.concepts(self.concept.name).casebases(casebase).instances.POST(params={'cases':json.dumps(case_data_json)})
        for reskey,dataval in zip(result.json(),case_data_json["cases"]):
            self.instances[reskey] = Instance(self.concept,reskey,self.host,casebase,dataval,get=True)

    def items(self):
        return self.instances.items()

    def instanceList(self):
        return list(self.instances.values())

    def deleteInstances(self,casebase):
        for key in self.instances:
            self.instances.pop(key)
        api = getRequest(self.host)
        api.concepts(self.concept.name).casebases(casebase).instances.DELETE()
