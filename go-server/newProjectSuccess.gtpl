<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>New Project | ML Data Collection Tool</title>
    <!-- Latest compiled and minified CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">


    <script src="http://code.jquery.com/jquery-3.2.1.min.js"></script>

    <!-- Latest compiled and minified JavaScript -->
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js" integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa" crossorigin="anonymous"></script>

    <link rel="stylesheet" href="./static/styles.css">
</head>
<body>
<div id="custom-navbar" class="navbar navbar-default navbar-fixed-top" role="navigation">
    <div class="container-fluid">
        <div class="navbar-header"><a class="navbar-brand" href="/">ML Data Collection Tool</a>
            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-menubuilder"><span class="sr-only">Toggle navigation</span><span class="icon-bar"></span><span class="icon-bar"></span><span class="icon-bar"></span>
            </button>
        </div>
        <div class="collapse navbar-collapse navbar-menubuilder">
            <ul class="nav navbar-nav navbar-right">
                <li><a href="#" data-toggle="modal" data-target="#myModal">Project: {{.CurrentProjectTitle}} &#9660;</a>
                </li>
            </ul>
        </div>
    </div>
</div>


<div style="padding-top: 50px;">
    <div class="row">
        <div class="col col-md-1"></div>
        <div class="col col-md-11">

            <h1>New Project</h1>
        </div>
    </div>
</div>
<div class="row">
    <div class="col col-md-1"></div>
    <div class="primaryCol col col-md-10">
        <h3>Project Successfully Added</h3>
    </div>
    <div class="col col-md-1"></div>
</div>


</div>



</div>





<footer class="footer">
    <div class="container">
        <p class="text-muted text-center">&copy; Timothy Cole 2018</p>
    </div>
</footer>




<!-- Modal -->
<div id="myModal" class="modal fade" role="dialog">
    <div class="modal-dialog">

        <!-- Modal content-->
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">&times;</button>
                <h4 class="modal-title">Project Selection</h4>
            </div>
            <div class="modal-body">
                <a class="btn btn-success" href="/newProject">New Project</a>
                <table class="table table-striped">
                    <tbody>
                    {{range .ProjectList}}
                    <tr>
                        <td>{{.Title}}</td>
                        <td><a class="btn btn-primary" href="/train?project={{.Id}}">Train</a></td>
                        <td><a class="btn btn-info" href="/result?project={{.Id}}">Results</a></td>
                    </tr>
                    {{end}}
                    </tbody>
                </table>
            </div>
        </div>

    </div>
</div>

</body>
</html>