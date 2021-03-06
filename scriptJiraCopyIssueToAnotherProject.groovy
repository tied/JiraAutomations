//Define in wich project must be created the issue when it is on this project specific status
def projectKeyToCreateIssue = "DES"

//Define in which status of this project will create the issue in another project
def statusTrigger = "REV FINAL"

//Get all issuetypes
def issueTypesReq = get("/rest/api/2/issuetype").asObject(List)

//Get issuetype required
def taskType = issueTypesReq.body.find { it.name == "Tarefa" }
def taskTypeId = taskType["id"]

//Get this issue status
def issueStatus = issue.fields['status']['name'] as String

//If this issue status is not what must be create on another project, finalize the process
if(issueStatus != statusTrigger){
    return
}

//Get issue summary (name)
def issueSummary = issue.fields['summary'] as String

//Create issue on another project with it summary (name) and type 
post("/rest/api/2/issue/bulk")
    .header("Content-Type", "application/json")
    .body(
    [
        issueUpdates: [
            [
                fields: [
                    summary    : issueSummary, 
                    description: "",
                    project    : [
                        key: projectKeyToCreateIssue
                    ],
                    issuetype  : [
                        id: taskTypeId
                    ]
                ]
            ]
        ]
    ])
    .asString()

/* Gabriel Tessarini */
