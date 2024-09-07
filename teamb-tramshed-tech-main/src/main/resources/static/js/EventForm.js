async function fetchVenues() {
    try {
        const response = await fetch('/api/proxy/venues', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            }
        });
        const data = await response.json();
        fillVenueOptions(data);
    } catch (error) {
        console.error('Error fetching venue data:', error);
    }
}


// 填充<select>元素的函数，前面提供的保持不变
function fillVenueOptions(data) {
    const venueSelect = document.getElementById('event-venue');
    venueSelect.innerHTML = ''; // 先清空现有选项
    data.results.forEach(venue => {
        const option = document.createElement('option');
        option.value = venue.id;
        option.textContent = venue.properties['Venue Name'].title[0].plain_text;
        venueSelect.appendChild(option);
    });
}


document.addEventListener('DOMContentLoaded', fetchVenues);


async function fetchAdmins() {
    try {
        const response = await fetch('/api/proxy/admins', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            }
        });
        const data = await response.json();
        fillAdminOptions(data);
    } catch (error) {
        console.error('Error fetching admin data:', error);
    }
}

function fillAdminOptions(data) {
    const adminSelect = document.getElementById('event-admin');
    adminSelect.innerHTML = '';
    data.results.forEach(admin => {
        console.log(admin)
        const option = document.createElement('option');
        option.value = admin.id;
        if (admin.properties['Name'] && admin.properties['Name'].title.length > 0) {
            option.textContent = admin.properties['Name'].title[0].plain_text;
        } else {
            option.textContent = 'gg';
        }
        adminSelect.appendChild(option);
    });
}

document.addEventListener('DOMContentLoaded', fetchAdmins);
