$(document).ready(function() {
    function getCookie(name) {
        let cookieArray = document.cookie.split('; ');
        let cookie = cookieArray.find(row => row.startsWith(name + '='));
        return cookie ? decodeURIComponent(cookie.split('=')[1].replace(/\+/g, ' ')) : null;
    }

    var userName = getCookie('userName');
    if (userName) {
        $('.username').text(userName);
    }
});

document.addEventListener('DOMContentLoaded', function() {

    var navLinks = document.querySelectorAll('.sidebar a');
    var activitySubmenu = document.getElementById('activitySubmenu');
    var JoinSubmenu = document.getElementById('JoinSubmenu');
    var CalendarSubmenu = document.getElementById('CalendarSubmenu');
    var calendarInitialized = false;


    navLinks.forEach(function(link) {
        link.addEventListener('click', function(event) {
            if (this.getAttribute('href') && this.getAttribute('href').endsWith('.html')) {
                return;
            }
            event.preventDefault();
            var target = this.getAttribute('data-target');
            var pageToShow = document.getElementById(target);
            if (pageToShow) {
                var pages = document.querySelectorAll('.page');
                pages.forEach(function(page) {
                    page.style.display = 'none';
                });
                pageToShow.style.display = 'block';

                if (target === 'CalendarPage' && !calendarInitialized) {
                    initializeCalendar();
                    calendarInitialized = true;
                }
            } else {
                console.error("No element found for ID:", target);
            }
            updateActiveMenu(target);
        });
    });

    function updateActiveMenu(target) {
        activitySubmenu.classList.toggle('active', target === 'dashboardPage');
        JoinSubmenu.classList.toggle('active', target === 'dashboardPage2');
        CalendarSubmenu.classList.toggle('active', target === 'CalendarPage');
    }


    // FullCalendar
    function initializeCalendar() {
        var calendarEl = document.getElementById('calendar');
        var calendar = new FullCalendar.Calendar(calendarEl, {
            initialView: 'dayGridMonth',
            headerToolbar: {
                left: 'prev,next today',
                center: 'title',
                right: 'dayGridMonth,timeGridWeek,timeGridDay'
            },
            nowIndicator: true,
            editable: true,
            selectable: true,
            height: 'parent',
            aspectRatio: 1.35,
            contentHeight: 'auto',
            events: [
                { title: 'Event 1', start: new Date().toISOString(), end: new Date().toISOString() }
            ]
        });

        calendar.render();
    }



    let allEvents = [];
    let allEvents2 = [];
    let allEvents3 = [];
    function loadEvents() {
        fetch('/events/myEvents')
            .then(response => {
                if (!response.ok) {
                    throw new Error('Failed to fetch events: ' + response.statusText);
                }
                return response.json();
            })
            .then(data => {
                console.log('Received data:', data);  // 查看接收到的数据
                allEvents = data || [];
                displayEvents(allEvents, 'dashboard');
            })
            .catch(error => console.error('Failed to load events:', error));
    }


    document.getElementById('searchInput').addEventListener('input', function() {
        const searchText = this.value.toLowerCase();
        const filteredEvents = allEvents.filter(event => event.properties["Event Name"].title[0].text.content.toLowerCase().includes(searchText));
        displayEvents(filteredEvents, 'dashboard');  // 重新显示匹配的事件
    });
    function displayEvents(events, containerId) {
        var container = document.getElementById(containerId);
        container.innerHTML = '';
        if (Array.isArray(events)) {  // 确保 events 是数组
            events.forEach(event => {
                console.log(event.properties)
                const eventName = event.properties["Event Name"].title[0].text.content;
                const startDate = new Date(event.properties.Date.date.start).toLocaleString();
                const endDate = new Date(event.properties.Date.date.end).toLocaleString();
                const eventType = event.properties["Event Type"].select.name;
                const cancelled = event.properties["Cancelled"].checkbox;  // 是否取消

                // 创建事件卡片
                var card = document.createElement('div');
                card.className = 'card';
                card.innerHTML = `
                <h3>${eventName}</h3>
                <p>Start Time: ${startDate}</p>
                <p>End Time: ${endDate}</p>
                <p>Event Type: ${eventType}</p>
                <p>Cancelled: ${cancelled ? 'Yes' : 'No'}</p>

                <div class="actions">
                    <button class="btn btn-primary btn-sm" onclick="viewDetails('${event.id}')">View Details</button>
                    <button class="btn btn-secondary btn-sm" onclick="editEvent('${event.id}')">Edit</button>
                    <button class="btn btn-danger btn-sm" onclick="cancelEvent('${event.id}')">Cancel</button>
                </div>
            `;
                container.appendChild(card);

                // 检查是否存在场地关系，并获取场地名称
                if (event.properties.Venue && event.properties.Venue.relation.length > 0) {
                    const venueId = event.properties.Venue.relation[0].id;
                    fetch(`/venues/${venueId}`)
                        .then(response => response.json())
                        .then(venue => {
                            const venueElement = card.querySelector('p:nth-child(5)');
                            // 确保 Venue Name 和 title 属性存在
                            if (venue.properties["Venue Name"] && venue.properties["Venue Name"].title.length > 0) {
                                venueElement.textContent = `Venue: ${venue.properties["Venue Name"].title[0].text.content}`;
                            } else {
                                venueElement.textContent = 'Venue: No name available';
                            }
                        })
                        .catch(error => {
                            console.error('Error fetching venue details:', error);
                            const venueElement = card.querySelector('p:nth-child(5)');
                            venueElement.textContent = 'Venue: No venue assigned';
                        });
                } else {
                    const venueElement = card.querySelector('p:nth-child(5)');
                    venueElement.textContent = 'Venue: No venue assigned';
                }
            });
        } else {
            console.error('Expected an array but received:', events);
        }
    }


    window.viewDetails = function(eventId) {
        fetch(`/events/${eventId}`)
            .then(response => response.json())
            .then(event => {
                document.getElementById('detailTitle').value = event.properties["Event Name"].title[0].text.content;
                document.getElementById('detailDate').value = `${new Date(event.properties.Date.date.start).toLocaleString()} - ${new Date(event.properties.Date.date.end).toLocaleString()}`;
                document.getElementById('detailVenue').value = event.properties.Venue.relation.length > 0 ? event.properties.Venue.relation[0].id : 'No venue assigned';
                document.getElementById('detailFoodRequirement').value = event.properties["Food Requirement"].multi_select.map(item => item.name).join(', ');
                document.getElementById('detailEventType').value = event.properties["Event Type"].select.name;
                document.getElementById('detailClient').value = event.properties.Client.relation.length > 0 ? event.properties.Client.relation[0].id : 'No client assigned';
                document.getElementById('detailEquipmentRequirement').value = event.properties["Equipment Requirement"].multi_select.map(item => item.name).join(', ');
                document.getElementById('detailAdmin').value = event.properties.Admin.relation.length > 0 ? event.properties.Admin.relation[0].id : 'No admin assigned';
                document.getElementById('detailStatus').value = event.properties.Status.status.name;
                document.getElementById('detailCancelled').value = event.properties.Cancelled.checkbox ? 'Yes' : 'No';
                document.getElementById('detailNumberOfParticipants').value = event.properties["The number of participants"].number;
                document.getElementById('detailExistingNum').value = event.properties.ExistingNUM.number;

                $('#viewDetailsModal').modal('show');
            })
            .catch(error => console.error('Error fetching event details:', error));
    }

    window.editEvent = function(eventId) {
        fetch(`/events/${eventId}`)
            .then(response => response.json())
            .then(event => {
                document.getElementById('eventId').value = event.id;
                document.getElementById('eventName').value = event.properties["Event Name"].title[0].text.content;
                document.getElementById('startTime').value = new Date(event.properties.Date.date.start).toISOString().slice(0, 16);
                document.getElementById('endTime').value = new Date(event.properties.Date.date.end).toISOString().slice(0, 16);
                document.getElementById('eventType').value = event.properties["Event Type"].select.name;

                $('#editEventModal').modal('show');
            })
            .catch(error => console.error('Error fetching event details:', error));
    }

    window.saveEvent = function() {
        const eventId = document.getElementById('eventId').value;
        const updatedEvent = {
            properties: {
                "Event Name": {
                    "title": [
                        { "text": { "content": document.getElementById('eventName').value } }
                    ]
                },
                "Date": {
                    "date": {
                        "start": new Date(document.getElementById('startTime').value).toISOString(),
                        "end": new Date(document.getElementById('endTime').value).toISOString()
                    }
                },
                "Event Type": {
                    "select": { "name": document.getElementById('eventType').value }
                }
            }
        };

        fetch(`/events/${eventId}`, {
            method: 'PATCH',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(updatedEvent)
        })
            .then(response => {
                if (response.ok) {
                    alert('Event updated successfully!');
                    $('#editEventModal').modal('hide');
                    location.reload();  // Optionally refresh the page or reload events
                } else {
                    alert('Failed to update event.');
                }
            })
            .catch(error => {
                console.error('Failed to save the event:', error);
                alert('Error saving event.');
            });
    }

    window.cancelEvent = function(eventId) {
        if (confirm('Are you sure you want to cancel this event?')) {
            fetch(`/events/${eventId}/cancel`, {
                method: 'PATCH',
                headers: {
                    'Content-Type': 'application/json'
                }
            })
                .then(response => {
                    if (response.ok) {
                        alert('Event cancelled successfully!');
                        loadEvents();  // Reload or refresh the event list
                    } else {
                        response.json().then(data => alert('Failed to cancel event: ' + data.message));
                    }
                })
                .catch(error => {
                    console.error('Error cancelling the event:', error);
                    alert('Error cancelling event.');
                });
        }
    };


    loadEvents();








    function loadJoinEvents() {
        fetch('/events/pp')
            .then(response => response.json())
            .then(data =>{
                allEvents2=data.results;
                displayEvents2(allEvents2, 'dashboard2')
            }) // 修改这里，传递data.results而不是data
            .catch(error => console.error('Failed to load events:', error));
    }

    document.getElementById('searchInput2').addEventListener('input', function() {
        const searchText = this.value.toLowerCase();
        const filteredEvents = allEvents2.filter(event => event.properties["Event Name"].title[0].text.content.toLowerCase().includes(searchText));
        displayEvents2(filteredEvents, 'dashboard2');  // 重新显示匹配的事件
    });

    function displayEvents2(events, containerId) {
        var container = document.getElementById(containerId);
        container.innerHTML = '';
        if (Array.isArray(events)) {  // 确保 events 是数组
            events.forEach(event => {
                const eventName = event.properties["Event Name"].title[0].text.content;
                const startDate = new Date(event.properties.Date.date.start).toLocaleString();
                const endDate = new Date(event.properties.Date.date.end).toLocaleString();
                const eventType = event.properties["Event Type"].select.name;
                const cancelled = event.properties["Cancelled"].checkbox;  // 是否取消

                // 创建事件卡片
                var card = document.createElement('div');
                card.className = 'card';
                card.innerHTML = `
                <h3>${eventName}</h3>
                <p>Start Time: ${startDate}</p>
                <p>End Time: ${endDate}</p>
                <p>Event Type: ${eventType}</p>
                <p>Cancelled: ${cancelled ? 'Yes' : 'No'}</p>

                <div class="actions">
                    <button class="btn btn-primary btn-sm" onclick="viewDetails('${event.id}')">View Details</button>
                    <button class="btn btn-danger btn-sm" onclick="ExitEvent('${event.id}')">Exit this event</button>
                </div>
            `;
                container.appendChild(card);

                // 检查是否存在场地关系，并获取场地名称
                if (event.properties.Venue && event.properties.Venue.relation.length > 0) {
                    const venueId = event.properties.Venue.relation[0].id;
                    fetch(`/venues/${venueId}`)
                        .then(response => response.json())
                        .then(venue => {
                            const venueElement = card.querySelector('p:nth-child(5)');
                            // 确保 Venue Name 和 title 属性存在
                            if (venue.properties["Venue Name"] && venue.properties["Venue Name"].title.length > 0) {
                                venueElement.textContent = `Venue: ${venue.properties["Venue Name"].title[0].text.content}`;
                            } else {
                                venueElement.textContent = 'Venue: No name available';
                            }
                        })
                        .catch(error => {
                            console.error('Error fetching venue details:', error);
                            const venueElement = card.querySelector('p:nth-child(5)');
                            venueElement.textContent = 'Venue: No venue assigned';
                        });
                } else {
                    const venueElement = card.querySelector('p:nth-child(5)');
                    venueElement.textContent = 'Venue: No venue assigned';
                }
            });
        } else {
            console.error('Expected an array but received:', events);
        }
    }
    loadJoinEvents()


    function loadwillJoinEvents() {
        fetch('/events/pp')
            .then(response => response.json())
            .then(data =>{
                allEvents3=data.results;
                displayEvents3(allEvents3, 'dashboard3')
            }) // 修改这里，传递data.results而不是data
            .catch(error => console.error('Failed to load events:', error));
    }

    document.getElementById('searchInput3').addEventListener('input', function() {
        const searchText = this.value.toLowerCase();
        const filteredEvents = allEvents3.filter(event => event.properties["Event Name"].title[0].text.content.toLowerCase().includes(searchText));
        displayEvents3(filteredEvents, 'dashboard3');  // 重新显示匹配的事件
    });

    function displayEvents3(events, containerId) {
        var container = document.getElementById(containerId);
        container.innerHTML = '';
        if (Array.isArray(events)) {  // 确保 events 是数组
            events.forEach(event => {
                const cancelled = event.properties["Cancelled"].checkbox;  // 是否取消

                if (!cancelled) {  // 仅当事件未取消时才显示
                    const eventName = event.properties["Event Name"].title[0].text.content;
                    const startDate = new Date(event.properties.Date.date.start).toLocaleString();
                    const endDate = new Date(event.properties.Date.date.end).toLocaleString();
                    const eventType = event.properties["Event Type"].select.name;

                    // 创建事件卡片
                    var card = document.createElement('div');
                    card.className = 'card';
                    card.innerHTML = `
                    <h3>${eventName}</h3>
                    <p>Start Time: ${startDate}</p>
                    <p>End Time: ${endDate}</p>
                    <p>Event Type: ${eventType}</p>
                `;

                    // 检查是否存在场地关系，并获取场地名称
                    if (event.properties.Venue && event.properties.Venue.relation.length > 0) {
                        const venueId = event.properties.Venue.relation[0].id;
                        fetch(`/venues/${venueId}`)
                            .then(response => response.json())
                            .then(venue => {
                                const venueElement = document.createElement('p');
                                venueElement.textContent = `Venue: ${venue.properties["Venue Name"].title[0].text.content}`;
                                card.appendChild(venueElement); // 确保场地信息在按钮上面
                                addActions(card, event.id); // 添加动作按钮
                            })
                            .catch(error => {
                                console.error('Error fetching venue details:', error);
                                const venueElement = document.createElement('p');
                                venueElement.textContent = 'Venue: No venue assigned';
                                card.appendChild(venueElement);
                                addActions(card, event.id);
                            });
                    } else {
                        const venueElement = document.createElement('p');
                        venueElement.textContent = 'Venue: No venue assigned';
                        card.appendChild(venueElement);
                        addActions(card, event.id);
                    }

                    container.appendChild(card);
                }
            });
        } else {
            console.error('Expected an array but received:', events);
        }
    }

    function addActions(card, eventId) {
        const actionsDiv = document.createElement('div');
        actionsDiv.className = 'actions';
        actionsDiv.innerHTML = `
        <button class="btn btn-primary btn-sm" onclick="viewDetails('${eventId}')">View Details</button>
        <button class="btn btn-success btn-sm" onclick="ExitEvent('${eventId}')">Join</button>
    `;
        card.appendChild(actionsDiv);
    }

    loadwillJoinEvents();
});

// function getCookie(name) {
//     let cookieArr = document.cookie.split(";");
//     for(let i = 0; i < cookieArr.length; i++) {
//         let cookiePair = cookieArr[i].split("=");
//         if(name == cookiePair[0].trim()) {
//             return decodeURIComponent(cookiePair[1]);
//         }
//     }
//     return null;
// }
//
// var userEmail = getCookie('userEmail');
// console.log(userEmail)
// if (userEmail) {
//     document.getElementById('user').textContent = userEmail;
// }

