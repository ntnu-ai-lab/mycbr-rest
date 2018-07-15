from mycbrwrapper.rest import getRequest

__name__ = "similarityfunctions"

def getAmalgationFuntcions(host,concept):
    """This function get the list of amalgationfuntcions from a given concept.

    :param host: hostname of the API server (e.g. epicmonolith.duckdns.org:8080)
    :param concept: the concept you want to add the amalgamationFunction to
    :returns: The amalagation functions of the concept or Null if not successfull.
    :rtype: list of amalgationFunction

    """
    api = getRequest(host)
    params = {'caseBase':'CB_csv_Import','attribute':concept}
    amalgationFuntcions = api.concepts.concept(concept).amalgamationFunctions.GET()
    return amalgationFuntcions

def addAmalagationFuntcion(host,concept):
    """This function adds a amalgation function to a concept
    :param host: hostname of the API server (e.g. epicmonolith.duckdns.org:8080)
    :param concept: the concept you want to add the amalgamationFunction to
    :returns: The added amalagation function or Null if not successfull.
    :rtype: amalgationFunction

    """
    api = getRequest(host)
    payload = {'caseBase':'CB_csv_Import','attribute':concept}
    api.concepts.concept(concept).amalagamationFuntcions.amalgationFuntcion(payload).PUT()
