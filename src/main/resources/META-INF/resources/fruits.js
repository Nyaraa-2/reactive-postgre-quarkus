function refresh() {
    $.get('/fruits', function (fruits) {
        var list = '';
        (fruits || []).forEach(function (fruit) {
            list = list
                    +'<tbody>'
                    +'<tr>'
                    +'<th scope="row">'+ fruit.id + '</th>'
                    +'<td>'+ fruit.name +'</td>'
                    + '<td><button type="button" class="btn btn-danger" onclick="deleteFruit(' + fruit.id + ')">Delete</button> <button type="button" class="btn btn-primary" onclick="updateFruit(' + fruit.id + ')">Update</button></td>'
                    +'</tr>'
                    +'</tbody>'
        });
        if (list.length > 0) {
            list = ''
                + '<table class="table text-center"><thead><th scope="col">ID</th><th scope="col">Name</th><th scope="col">Action</th></thead>'
                + list
                + '</table>';
        } else {
            list = "No fruits in database"
        }
        $('#all-fruits').html(list);
    });
}

function deleteFruit(id) {
    $.ajax('/fruits/' + id, {method: 'DELETE'}).then(refresh);
}

$(document).ready(function () {

    $('#create-fruit-button').click(function () {
        var fruitName = $('#fruit-name').val();
        $.post({
            url: '/fruits',
            contentType: 'application/json',
            data: JSON.stringify({name: fruitName})
        }).then(refresh);
    });

    refresh();
});