<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Results | {{.CurrentProjectTitle}}</title>
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

            <h1>Results</h1></div>
    </div>

</div>
<div class="row">
    <div class="col col-md-1"></div>

    <div class="primaryCol col col-md-10">
        <div class="row">
            <div class="text-right">
                <a href="/generateCSV?project={{.CurrentProjectID}}">Download CSV</a>
            </div>
        </div>
    <div class="row">
        <table class="table">
            <col width="400">
            <col width="800">
            <col width="400">
            <tr>
                <th>Question</th>
                <th>Paraphrase</th>
                <th>Answer</th>
            </tr>


        {{range .FAQs}}
            <tr>
                <td rowspan="{{add (len .Paraphrases) 1}}">{{.Question}}</td>
            {{if eq (len .Paraphrases) 0}}
                <td></td>
            {{else}}
                <td style="display: inherit;"></td>
            {{end}}
                <td rowspan="{{add (len .Paraphrases) 1}}">{{.Answer}}</td>
            </tr>

        {{range .Paraphrases}}
            <tr>
                <td rowspan="1">{{.ParaphraseText}}</td>
            </tr>
        {{end}}

        {{end}}
        </table>

    </div>

</div>
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