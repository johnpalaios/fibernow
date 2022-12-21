const url = 'http://localhost:8080/fibernow/api/v1/tickets/top10';
const credentials = 'Basic ' + btoa('admin:admin');


function get10Pending() {
    fetch(url, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': credentials
        },
    })
        .then((response) => response.json())
        .then((data) => { displayPending(data.data); })
        .catch((error) => { console.error('Network Error: ', error); });
}

function displayPending(data) {
    document.getElementById("pending").innerHTML = "";
    var columnNames = ['Ticket ID', 'Customer ID', 'Received date', 'Scheduled date', 'Ticket status', 'Ticket type', 'Estimated Cost', 'Address', 'Description'];
    var div = document.getElementById("pending");
    var table = document.createElement("table");

    var tr = document.createElement('tr');
    for (var i = 0; i < columnNames.length; i++) {
        var th = document.createElement('th');
        var text = document.createTextNode(columnNames[i]);
        th.appendChild(text);
        tr.appendChild(th);
    }
    table.appendChild(tr);

    div.appendChild(table);


    for (var j = 0; j < data.length; j++) {
        var tr = document.createElement('tr');
        var dataValues = data[j];
        dataValues = Object.values(dataValues);
        for (var i = 0; i < columnNames.length + 1; i++) {
            if (i < columnNames.length) {
                var td = document.createElement('td');
                var input = document.createElement("input");
                input.setAttribute('type', 'text');
                // input.id = dataValues[0] + "editInput";
                // input.setAttribute("required", "");
                input.value = dataValues[i];
                input.disabled = true;
                td.appendChild(input);
                tr.appendChild(td);
            } else {
                // var td = document.createElement('td');
                // var editBtn = document.createElement("button");
                // var deleteBtn = document.createElement("button");
                // editBtn.className = "editBtns";
                // //editBtn.addEventListener("click", editButton(editBtn));
                // deleteBtn.className = "deleteBtns";
                // editBtn.id = dataValues[0] + "editBtn";
                // deleteBtn.id = dataValues[0] + "deleteBtn";
                // //deleteBtn.addEventListener("click", deleteButton(deleteBtn));
                // var editText = document.createTextNode("Edit");
                // var deleteText = document.createTextNode("Delete");
                // editBtn.appendChild(editText);
                // deleteBtn.appendChild(deleteText);
                // td.appendChild(editBtn);
                // td.appendChild(deleteBtn);
                // tr.appendChild(td);
                // //editBtns.push(editBtn);
                // //deleteBtns.push(deleteBtn);
            }
        }
        table.appendChild(tr);
    }

}