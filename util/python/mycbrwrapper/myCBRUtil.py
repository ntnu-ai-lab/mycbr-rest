from mycbrwrapper.rest import getRequest

def clearMyCBR(host):
    api = getRequest(host)
    call = api.concepts
    result = call.GET()
    concepts = result.json().get("concept")
    for elem in concepts:
        api.concepts(elem).DELETE()