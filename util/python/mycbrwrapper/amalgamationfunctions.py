from mycbrwrapper.rest import getRequest

class AmalgamationFunction():

    def __init__(self, host, name, parameters, concept, superCall=True):
        print("in normal amalgamationfunction creator name is: {}".format(name))
        self.host = host
        self.name = name
        self.concept = concept
        self.parameters = parameters
        if superCall is not True:
            self.createAmalgamationFunction()

    def createAmalgamationFunction(self):
        print("in createamal")
        api = getRequest(self.host)
        call = api.concepts(self.concept.name).amalgamationFunctions(self.name)
        result = call.PUT(params=self.parameters)
        self.created = True

    def delete(self):
        self.name = "deleted"

class NeuralAmalgamationFunction(AmalgamationFunction):

    def __init__(self, host, name, filenames, concept):
        print("in neuralamalgamationfunction creator filenames is: {}".format(filenames))
        super(NeuralAmalgamationFunction, self).__init__(host, name, None, concept)
        print("in neuralamalgamationfunction 2")
        self.files = filenames
        self.createAmalgamationFunction()

    def createAmalgamationFunction(self):
        print("in NEURAL createamal")
        api = getRequest(self.host)
        call = api.concepts(self.concept.name).neuralAmalgamationFunctions(self.name)
        jsonfilename = self.files["json"]
        h5filename = self.files["h5"]
        files = {"jsonfile": (jsonfilename,
                              open(jsonfilename,'rb'), {'Expires': '0'}),
                 "h5file": (h5filename,
                            open(h5filename,'rb'), {'Expires': '0'})}
        #requests.post('http://requestb.in/xucj9exu', files=(('foo', 'bar'), ('spam', 'eggs')))
        #result = call.PUT('http://requestb.in/xucj9exu', files=(('foo', 'bar'), ('spam', 'eggs')))
        print("in neural createamalgamationfunction url is: {}".format(call._url))
        results = call.PUT(files={"jsonfile":(jsonfilename,open(jsonfilename,'rb'),{'Expires': '0'}),
                                  "h5file":(h5filename,open(h5filename,'rb'),{'Expires': '0'})})
        #results = call.PUT(files=files)
        #results = call.PUT(params=files)
        print("results: {}".format(results))
        self.created = True
