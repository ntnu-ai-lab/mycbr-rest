from mycbrwrapper.rest import getRequest


class CaseBase():
    def __init__(self, concept, name, host, get=False):
        self.name = name
        self.concept = concept
        self.host = host
        self.createCaseBase()

    def createCaseBase(self):
        api = getRequest(self.host)
        call = api.casebases
        call.PUT(self.name)
