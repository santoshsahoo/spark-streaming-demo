// request permission on page load
document.addEventListener('DOMContentLoaded', function() {
  if (Notification.permission !== "granted")
    Notification.requestPermission();

  if (!Notification) {
    alert('Desktop notifications not available in your browser. Try Chrome/FF.');
    return;
  }
});

function notifyMe(id) {
  if (Notification.permission !== "granted")
    Notification.requestPermission();
  else {
    var notification = new Notification('Fraud noticed', {
      icon: 'https://lh5.ggpht.com/p-SI-FKdXrZ33jpaF08K8sG9QiQVYCsqggS8LyjLSsUS7Fy9YqUeegt0WwRPd4ccfsA=w300',
      body: "A transaction looks fraudlent. id " + id,
    });

    notification.onclick = function() {
      window.open("http://localhost:3000/fraud/" + id);
    };

  }
}
