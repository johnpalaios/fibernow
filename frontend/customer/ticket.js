const url = 'http://localhost:8080/fibernow/api/v1/tickets';
const log = (msg) => console.log(msg);
const credentials = 'Basic ' + btoa('admin:admin');


const createModal = document.getElementById("createModal");
const closeModal = document.querySelector("#closeCreateModal");

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

function searchTickets(startDate, endDate) {
    const params = {
        // customerId: customerId.value,
        customerId: 2,
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
        .then((data) => { displaySearch(data.data); })
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
        .then((data) => { console.log(data); })
        .then(searchModal.style.display = "none")
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
    let td;
    let text;
    let th;
    log(data);
    var ok = document.getElementById("searchDetails");
    for (const child of ok.children) {
        child.innerHTML = "";
    }

    const columnNames = ['Ticket ID', 'Customer ID', 'Received date', 'Scheduled date', 'Ticket status', 'Ticket type', 'Estimated Cost', 'Address', 'Description'];
    const table = document.getElementById("searchDetails");
    let tr = document.createElement('tr');
    for (let i = 0; i < columnNames.length; i++) {
        th = document.createElement('th');
        text = document.createTextNode(columnNames[i]);
        th.appendChild(text);
        tr.appendChild(th);
    }
    table.appendChild(tr);

    for (let j = 0; j < data.length; j++) {
        tr = document.createElement('tr');
        let dataValues = data[j];
        dataValues = Object.values(dataValues);
        for (let i = 0; i < columnNames.length; i++) {
            td = document.createElement('td');
            const input = document.createElement("input");
            input.setAttribute('type', 'text');
            input.id = dataValues[j] + "editInput";
            input.setAttribute("required", "");
            input.className = "table-input";
            input.value = dataValues[i];
            input.disabled = true;
            td.appendChild(input);
            tr.appendChild(td);
        }
        table.appendChild(tr);
    }
}