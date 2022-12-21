const url = 'http://localhost:8080/fibernow/api/v1/tickets/top10';
const credentials = 'Basic ' + btoa('admin:admin');

get10Pending();
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
    document.getElementById("pending-details").innerHTML = "";
    var columnNames = ['Ticket ID', 'Customer ID', 'Received date', 'Scheduled date', 'Ticket status', 'Ticket type', 'Estimated Cost', 'Address', 'Description'];
    var div = document.getElementById("pending");
    var table = document.getElementById("pending-details");

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
                input.className = "table-input";
                input.setAttribute('type', 'text');
                input.value = dataValues[i];
                input.disabled = true;
                td.appendChild(input);
                tr.appendChild(td);
            } else {
            }
        }
        table.appendChild(tr);
    }

}