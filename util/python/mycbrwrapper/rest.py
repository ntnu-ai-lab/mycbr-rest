import hammock

__name__ = "rest"

def getRequest(host):
    """This function adds a amalgation function to a concept

    :param host: hostname of the API server (e.g. epicmonolith.duckdns.org:8080)
    :returns: The hammock object for CBR REST API
    :rtype: hammock object for CBR REST API

    """
    api = hammock.Hammock("http://{}".format(host))
    return api 

