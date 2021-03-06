//Define the labels with name of each user and set it slack conversation links
def slacks = [
    ["name": "ELVIS", "link": "https://hooks.slack.com/services/XXXXXXX/YYYYYYY/ZZZZZZZZZZZZZZZZZZZ"],
    ["name": "PRISCILLA ", "link": "https://hooks.slack.com/services/XXXXXXX/YYYYYYY/ZZZZZZZZZZZZZZZZZZZ"]
]

//Get the issue of comment
def thisIssue = get("/rest/api/2/issue/" + issue["id"] + "")
    .asObject(Map)
    .body
   
//Get all label values in this issue
def labels = thisIssue.fields["labels"]
//Get issue summary
def summary = thisIssue.fields["summary"];
// Check if is a worklog of a task or a subtask
if(thisIssue != null && thisIssue.fields["issuetype"]["subtask"] == true){
    summary = thisIssue.fields["parent"].fields["summary"].toString() + " -> " + summary + "";
	//Get parent label if subtask's is not filled
    if(labels.size() == 0){
        labels =  get("/rest/api/2/issue/" + thisIssue.fields["parent"]["id"] + "")
                    .asObject(Map)
                    .body
                    .fields["labels"];
    }
}
//Send a Slack for each user with label in this issue
for(label in labels){
    for(slack in slacks){
        if(slack.name == label){
            post(slack.link)
                .header("Content-Type", "application/json")
                .body(
                    [
                        text: "JIRA -> *" + summary + "*:\n"+ "https://business.atlassian.net/browse/" + issue["key"] + "\n"+ " You receive a comment from user '" + comment.author.displayName + "':\n" + comment.body
                    ]                    
                )
                .asString()
        }
    }
}

/* Gabriel Tessarini */
