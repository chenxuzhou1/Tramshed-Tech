document.addEventListener('DOMContentLoaded', function() {
    // 全局变量
    let allActivities = [];
    let allVenues = [];

    // 切换内容的函数
    function showContent(contentId) {
        var pages = document.querySelectorAll('.content-page');
        pages.forEach(function(page) {
            page.style.display = 'none';
        });
        document.getElementById(contentId).style.display = 'block';

        // 根据显示的内容加载不同的数据
        if (contentId === 'all') {
            loadAllActivities();
        } else if (contentId === 'venue') {
            loadAllVenues();
        }
    }

    // 加载所有活动的函数
    function loadAllActivities() {
        fetch('/events/pp')
            .then(response => response.json())
            .then(data => {
                allActivities = data.results || [];
                displayActivities(allActivities, 'activityDashboard');
            })
            .catch(error => console.error('Failed to load activities:', error));
    }

    // 加载所有场地的函数
    function loadAllVenues() {
        fetch('/venues/pp')
            .then(response => response.json())
            .then(data => {
                allVenues = data.results || [];
                displayVenues(allVenues, 'venueDashboard');
            })
            .catch(error => console.error('Failed to load venues:', error));
    }

    // 显示活动的函数
    function displayActivities(activities, containerId) {
        var container = document.getElementById(containerId);
        container.innerHTML = '';
        if (Array.isArray(activities)) {
            activities.forEach(activity => {
                const activityName = activity.properties["Event Name"].title[0].text.content;
                const startDate = new Date(activity.properties.Date.date.start).toLocaleString();
                const endDate = new Date(activity.properties.Date.date.end).toLocaleString();
                const activityType = activity.properties["Event Type"].select.name;
                const cancelled = activity.properties["Cancelled"].checkbox;

                var card = document.createElement('div');
                card.className = 'card';
                card.innerHTML = `
                    <h3>${activityName}</h3>
                    <p>Start Time: ${startDate}</p>
                    <p>End Time: ${endDate}</p>
                    <p>Event Type: ${activityType}</p>
                    <p>Cancelled: ${cancelled ? 'Yes' : 'No'}</p>

                    <div class="actions">
                        <button class="btn btn-primary btn-sm" onclick="viewDetails('${activity.id}')">View Details</button>
                        <button class="btn btn-secondary btn-sm" onclick="editEvent('${activity.id}')">Edit</button>
                        <button class="btn btn-danger btn-sm" onclick="cancelEvent('${activity.id}')">Cancel</button>
                    </div>
                `;
                container.appendChild(card);

                if (activity.properties.Venue && activity.properties.Venue.relation.length > 0) {
                    const venueId = activity.properties.Venue.relation[0].id;
                    fetch(`/venues/${venueId}`)
                        .then(response => response.json())
                        .then(venue => {
                            const venueElement = document.createElement('p');
                            if (venue.properties["Venue Name"] && venue.properties["Venue Name"].title.length > 0) {
                                venueElement.textContent = `Venue: ${venue.properties["Venue Name"].title[0].text.content}`;
                            } else {
                                venueElement.textContent = 'Venue: No name available';
                            }
                            card.appendChild(venueElement);
                        })
                        .catch(error => {
                            console.error('Error fetching venue details:', error);
                            const venueElement = document.createElement('p');
                            venueElement.textContent = 'Venue: No venue assigned';
                            card.appendChild(venueElement);
                        });
                } else {
                    const venueElement = document.createElement('p');
                    venueElement.textContent = 'Venue: No venue assigned';
                    card.appendChild(venueElement);
                }
            });
        } else {
            console.error('Expected an array but received:', activities);
        }
    }

    // 显示场地的函数
    function displayVenues(venues, containerId) {
        var container = document.getElementById(containerId);
        container.innerHTML = '';
        if (Array.isArray(venues)) {
            venues.forEach(venue => {
                const venueName = venue.properties["Venue Name"].title[0].text.content;
                const location = venue.properties.Location.rich_text[0].text.content;
                const capacity = venue.properties.Capacity.number;

                var card = document.createElement('div');
                card.className = 'card';
                card.innerHTML = `
                    <h3>${venueName}</h3>
                    <p>Location: ${location}</p>
                    <p>Capacity: ${capacity}</p>

                    <div class="actions">
                        <button class="btn btn-primary btn-sm" onclick="viewVenueDetails('${venue.id}')">View Details</button>
                        <button class="btn btn-secondary btn-sm" onclick="editVenue('${venue.id}')">Edit</button>
                        <button class="btn btn-danger btn-sm" onclick="deleteVenue('${venue.id}')">Delete</button>
                    </div>
                `;
                container.appendChild(card);
            });
        } else {
            console.error('Expected an array but received:', venues);
        }
    }

    window.viewDetails = function(activityId) {
        fetch(`/events/${activityId}`)
            .then(response => response.json())
            .then(activity => {
                document.getElementById('detailTitle').value = activity.properties["Event Name"].title[0].text.content;
                document.getElementById('detailDate').value = `${new Date(activity.properties.Date.date.start).toLocaleString()} - ${new Date(activity.properties.Date.date.end).toLocaleString()}`;
                document.getElementById('detailVenue').value = activity.properties.Venue.relation.length > 0 ? activity.properties.Venue.relation[0].id : 'No venue assigned';
                document.getElementById('detailFoodRequirement').value = activity.properties["Food Requirement"].multi_select.map(item => item.name).join(', ');
                document.getElementById('detailEventType').value = activity.properties["Event Type"].select.name;
                document.getElementById('detailClient').value = activity.properties.Client.relation.length > 0 ? activity.properties.Client.relation[0].id : 'No client assigned';
                document.getElementById('detailEquipmentRequirement').value = activity.properties["Equipment Requirement"].multi_select.map(item => item.name).join(', ');
                document.getElementById('detailAdmin').value = activity.properties.Admin.relation.length > 0 ? activity.properties.Admin.relation[0].id : 'No admin assigned';
                document.getElementById('detailStatus').value = activity.properties.Status.status.name;
                document.getElementById('detailCancelled').value = activity.properties.Cancelled.checkbox ? 'Yes' : 'No';
                document.getElementById('detailNumberOfParticipants').value = activity.properties["The number of participants"].number;
                document.getElementById('detailExistingNum').value = activity.properties.ExistingNUM.number;

                $('#viewDetailsModal').modal('show');
            })
            .catch(error => console.error('Error fetching activity details:', error));
    }

    window.editEvent = function(activityId) {
        fetch(`/events/${activityId}`)
            .then(response => response.json())
            .then(activity => {
                document.getElementById('eventId').value = activity.id;
                document.getElementById('eventName').value = activity.properties["Event Name"].title[0].text.content;
                document.getElementById('startTime').value = new Date(activity.properties.Date.date.start).toISOString().slice(0, 16);
                document.getElementById('endTime').value = new Date(activity.properties.Date.date.end).toISOString().slice(0, 16);
                document.getElementById('eventType').value = activity.properties["Event Type"].select.name;

                $('#editEventModal').modal('show');
            })
            .catch(error => console.error('Error fetching activity details:', error));
    }

    window.cancelEvent = function(activityId) {
        if (confirm('Are you sure you want to cancel this activity?')) {
            fetch(`/events/${activityId}/cancel`, {
                method: 'PATCH',
                headers: {
                    'Content-Type': 'application/json'
                }
            })
                .then(response => {
                    if (response.ok) {
                        alert('Activity cancelled successfully!');
                        loadAllActivities();
                    } else {
                        response.json().then(data => alert('Failed to cancel activity: ' + data.message));
                    }
                })
                .catch(error => {
                    console.error('Error cancelling the activity:', error);
                    alert('Error cancelling activity.');
                });
        }
    }

    window.viewVenueDetails = function(venueId) {
        // 实现查看场地详情的逻辑
        alert('Viewing details for venue: ' + venueId);
    }

    window.editVenue = function(venueId) {
        // 实现编辑场地的逻辑
        alert('Editing venue: ' + venueId);
    }

    window.deleteVenue = function(venueId) {
        // 实现删除场地的逻辑
        alert('Deleting venue: ' + venueId);
    }

    document.getElementById('searchInputAll').addEventListener('input', function() {
        const searchText = this.value.toLowerCase();
        const filteredActivities = allActivities.filter(activity => activity.properties["Event Name"].title[0].text.content.toLowerCase().includes(searchText));
        displayActivities(filteredActivities, 'activityDashboard');
    });

    document.getElementById('searchInputVenue').addEventListener('input', function() {
        const searchText = this.value.toLowerCase();
        const filteredVenues = allVenues.filter(venue => venue.properties["Venue Name"].title[0].text.content.toLowerCase().includes(searchText));
        displayVenues(filteredVenues, 'venueDashboard');
    });

    // 绑定按钮点击事件
    document.getElementById('activityAll').addEventListener('click', function() {
        showContent('all');
    });
    document.getElementById('activityYour').addEventListener('click', function() {
        showContent('activity');
    });
    document.getElementById('venueManage').addEventListener('click', function() {
        showContent('venue');
    });
    document.getElementById('accountManage').addEventListener('click', function() {
        showContent('account');
    });

    // 初始化默认页面并加载所有活动
    showContent('all');
});
