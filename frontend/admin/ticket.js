const url = 'http://localhost:8080/fibernow/api/v1/tickets';
const log = (msg) => console.log(msg);
const credentials = 'Basic ' + btoa('admin:admin');
const form = document.getElementById('ticket-create');
form.addEventListener('submit', createTicket);


const createModal = document.getElementById("createModal");
const createTicketBtn = document.getElementById("createTicketBtn");
const closeModal = document.querySelector("#closeCreateModal");
createTicketBtn.onclick = function () {
    createModal.style.display = "block";
}
closeModal.onclick = function () {
    document.getElementById("createTicketDetails").innerHTML = "";
    createModal.style.display = "none";
}

window.onclick = function (event) {
    if (event.target === createModal) {
        document.getElementById("createTicketDetails").innerHTML = "";
        createModal.style.display = "none";
    }
}
const searchBtn = document.getElementById("searchBtn");

function createTicket(event) {
    event.preventDefault();
    const payload = new FormData(form);
    const payloadObj = {};
    payload.forEach((value, key) => (payloadObj[key] = value));

    fetch(url, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json;charset=utf-8',
            'Authorization': credentials
        },
        body: JSON.stringify(payloadObj),
    })
        .then(response => response.json())
        .then(response => (response.baseErrors === null) ? alert("Ticket created successfully") : alert(response.baseErrors[0].message))
        .then(answer => log(answer))
        .then(form.reset())
        .catch(error => console.error('Network Error...', error));
}

function getTicketId() {
    let ticketId = document.getElementById('find-id').value;
    const urlId = url + "/" + ticketId;
    fetch(urlId, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': credentials
        },
    })
        .then((response) => response.json())
        .then((data) => { displayData(data.data) })
        .catch((error) => { console.error('Network Error: ', error); });
    document.getElementById('find-id').value = "";
}

function searchTickets(customerId, startDate, endDate) {
    const params = {
        customerId: customerId.value,
        startDate: startDate.value,
        endDate: endDate.value
    };
    let urlSearch = url + '?';
    const nonEmpty = {};
    Object.keys(params).forEach(key => {
        if (params[key] !== "") {
            nonEmpty[key] = params[key];
        }
    });
    urlSearch += (new URLSearchParams(nonEmpty)).toString();
    // log(urlSearch);
    fetch(urlSearch, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': credentials
        },
    })
        .then((response) => response.json())
        .then((data) =>  (data === null) ? alert("EEEEE") : displaySearch(data.data))
        .catch((error) => { console.error('Network Error: ', error); });
}

function deleteTicket(ticketId) {
    const urlId = url + "/" + ticketId;

    fetch(urlId, {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': credentials
        }
    })
        .then((response) => response.json())
        .then((response) => (response.baseErrors === null) ? alert("Ticket deleted successfully") : alert("Could not be deleted."))
        .catch((error) => { console.error('Network Error: ', error); });
}


function displayData(data) {
    if (data === null) {
        document.getElementById("ticketDetails").innerHTML = `No ticket with such ID found.`;
    } else {
        document.getElementById("ticketDetails").innerHTML = "";
        document.getElementById("ticketDetails").innerHTML +=
            "<p>Ticket ID: " + data.id + "<p>" +
            "<p>Customer ID: " + data.customerId + "<p>" +
            "<p>Received date: " + data.receivedDate + "<p>" +
            "<p>Scheduled date: " + data.scheduledDatetime + "<p>" +
            "<p>Ticket status: " + data.ticketStatus + "<p>" +
            "<p>Ticket type: " + data.type + "<p>" +
            "<p>Estimated cost: " + data.estimatedCost + "<p>" +
            "<p>Address: " + data.address + "<p>" +
            "<p>Description: " + data.description + "<p>";
    }
}

function displaySearch(data) {
    var ok = document.getElementById("searchDetails");
    for (const child of ok.children) {
        child.innerHTML = "";
    }

    var columnNames = ['Ticket ID', 'Customer ID', 'Received date', 'Scheduled date', 'Ticket status', 'Ticket type', 'Estimated Cost', 'Address', 'Description'];
    var table = document.getElementById("searchDetails");
    var tr = document.createElement('tr');
    for (var i = 0; i < columnNames.length; i++) {
        var th = document.createElement('th');
        var text = document.createTextNode(columnNames[i]);
        th.appendChild(text);
        tr.appendChild(th);
    }
    table.appendChild(tr);
    var th = document.createElement('th');
    var text = document.createTextNode('Actions');
    th.appendChild(text);
    tr.appendChild(th);
    table.appendChild(tr);

    for (var j = 0; j < data.length; j++) {
        var tr = document.createElement('tr');
        var dataValues = data[j];
        dataValues = Object.values(dataValues);
        for (var i = 0; i < columnNames.length + 1; i++) {
            if (i < columnNames.length) {
                var td = document.createElement('td');
                var input = document.createElement("input");
                input.setAttribute('type', 'text');
                input.id = dataValues[0] + "editInput";
                input.setAttribute("required", "");
                input.className = "table-input";
                input.value = dataValues[i];
                input.disabled = true;
                td.appendChild(input);
                tr.appendChild(td);
            } else {
                var td = document.createElement('td');
                var editBtn = document.createElement("i");
                var deleteBtn = document.createElement("i");
                editBtn.className = "fa fa-edit btn-edit margin-inline-2";
                editBtn.addEventListener("click", editButton(editBtn));
                deleteBtn.className = "fa fa-trash btn-delete margin-inline-2";
                editBtn.id = dataValues[0] + "editBtn";
                deleteBtn.id = dataValues[0] + "deleteBtn";
                deleteBtn.addEventListener("click", deleteButton(deleteBtn));
                td.appendChild(editBtn);
                td.appendChild(deleteBtn);
                tr.appendChild(td);
            }
        }
        table.appendChild(tr);
    }
}

function editButton(editBtn) {
    var count = 0;
    return function () {
        count += 1;
        var id = parseInt(editBtn.id);
        
        var editInput = id + "editInput";
        var forms = document.querySelectorAll(`[id="${editInput}"]`);
        if (count % 2 == 1) {
            for (var i = 0; i < forms.length; i++) {
                if (i == 0) continue;
                forms[i].disabled = false;
            }
        }
        if (count % 2 == 0) {
            for (var i = 0; i < forms.length; i++) {
                forms[i].disabled = true;
            }
            updateTicket(forms);
        }
    }
}

function deleteButton(deleteBtn) {
    var ticketId = parseInt(deleteBtn.id);
    return function () {
        deleteTicket(ticketId);
    }
}

function updateTicket(data) {
    var payload = {};
    payload.id = data[0].value;
    payload.customerId = data[1].value;
    payload.receivedDate = data[2].value;
    payload.scheduledDatetime = data[3].value;
    payload.ticketStatus = data[4].value;
    payload.type = data[5].value;
    payload.estimatedCost = data[6].value;
    payload.address = data[7].value;
    payload.description = data[8].value;

    fetch(url, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json;charset=utf-8',
            'Authorization': credentials
        },
        body: JSON.stringify(payload),
    })
        .then((response) => response.json())
        .then((response) => (response.baseErrors === null) ? alert(`Updated Ticket with ID: ${payload.id}`) : alert("Could not be updated."))
        .then(answer => console.log(answer))
        .catch(error => console.error('Network Error: ', error));
}