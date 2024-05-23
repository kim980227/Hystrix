/**
 * SpringBoot4
 *
 * @author shjang02 < shjang02@simplexi.com >
 * @since 2023. 06. 21.
 */

const server = 'http://43.202.66.154:8081/sb';

document.addEventListener("DOMContentLoaded", function () {

  let grid;
  let listData = {};
  fetch(`${server}/users`)
  .then(response => {
    // 오류 처리
    if (!response.ok) {
      throw new Error('Network response was not ok');
    }
    return response.json(); // json으로 변환
  })
  .then(data => {
    // 사용하려는 데이터
    console.log(data);
    listData = data;
    grid = new tui.Grid({
      el: document.getElementById('grid'),
      data: listData,
      scrollX: false,
      scrollY: false,
      columns: [
        {header: 'id', name: 'userId'},
        {header: 'Name', name: 'name'},
        {header: 'Password', name: 'password'},
        {header: 'approved', name: 'approved'},
      ]
    });
  })
  .catch(error => {
    console.error('There has been a problem with your fetch operation:', error);
  });

  document.getElementById('add-row').addEventListener('click', function () {
    var id = grid.getRowCount() + 2;
    fetch(`${server}/user`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        userId: 'newUser' + id,
        password: '1234',
        isApproved: true,
        name: 'New User' + id
      }),
    })
    .then(response => response.json())
    .then(data => {
      fetchData();
    })
    .catch((error) => {
      console.error('Error:', error);
    });
  });

  // Fetch data from server
  function fetchData() {
    fetch(`${server}/users`)
    .then(response => response.json())
    .then(data => {
      grid.resetData(data);
    })
    .catch((error) => {
      console.error('Error:', error);
    });
  }

  document.getElementById('remove-row').addEventListener('click', function () {
    var lastRow = grid.getFocusedCell().value;
    fetch(`${server}/user/${lastRow}`, {
      method: 'DELETE'
    })
    .then(() => {
      fetchData();
    })
    .catch((error) => {
      console.error('Error:', error);
    });
  });
});
