const url = 'http://localhost:8080/fibernow/api/v1/tickets';
const log = (msg) => console.log(msg);
const credentials = 'Basic ' + btoa('admin:admin');
const form = document.getElementById('ticket-create');
form.addEventListener('submit', createTicket);

function createTicket(event) {
    event.preventDefault();
    var payload = new FormData(form);
    var payloadObj = {};
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
        .then(answer => console.table(answer))
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
        .then((data) => { console.log(data); })
        .catch((error) => { console.error('Network Error: ', error); });
    document.getElementById('find-id').value = "";
}

function searchTickets(customerId, startDate, endDate) {
    var params = {
        customerId: customerId.value,
        startDate: startDate.value,
        endDate: endDate.value
    };
    var urlSearch = url + '?';
    var nonEmpty = {};
    Object.keys(params).forEach(key => {
        if (params[key] !== "") {
            nonEmpty[key] = params[key];
        }
    });
    urlSearch += (new URLSearchParams(nonEmpty)).toString();

    fetch(urlSearch, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': credentials
        },
    })
        .then((response) => response.json())
        .then((data) => { console.log(data); })
        .catch((error) => { console.error('Network Error: ', error); });
    document.getElementById('startDate').value = "";
    document.getElementById('endDate').value = "";
    document.getElementById('customerId').value = "";
}

function deleteTicket() {
    let ticketId = document.getElementById('delete-id').value;
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
        .catch((error) => { console.error('Network Error: ', error); });
    document.getElementById('delete-id').value = "";
}