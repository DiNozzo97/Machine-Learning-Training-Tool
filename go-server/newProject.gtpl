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
    <script>
        function projectTypeToggle() {
            var projectType = document.getElementById("projectType");
            var linkedFAQContainer0 = document.getElementById("FAQLinkContainer0");
            var linkedFAQContainer1 = document.getElementById("FAQLinkContainer1");

            if (projectType.value == "single-level-menu" || projectType.value == "two-level-menu") {
                linkedFAQContainer0.innerHTML =
                        "<fieldset>" +
                        "<legend>Top Menu Link Selector</legend>" +
                        "Selector Type: <input type=\"radio\" name=\"selectorType-faq_instance_link_0\" value=\"XPath\" checked> XPath <input type=\"radio\" name=\"selectorType-faq_instance_link_0\" value=\"CSS\"> CSS <br>\n" +
                        "Selector Path: <input type=\"text\" name=\"selectorPath-faq_instance_link_0\" required><br>" +
                        "</fieldset>";
            } else {
                linkedFAQContainer0.innerHTML = "";
            }

            if (projectType.value == "two-level-menu") {
                linkedFAQContainer1.innerHTML =
                        "<fieldset>" +
                        "<legend>Sub Menu Link Selector</legend>" +
                        "Selector Type: <input type=\"radio\" name=\"selectorType-faq_instance_link_1\" value=\"XPath\" checked> XPath <input type=\"radio\" name=\"selectorType-faq_instance_link_1\" value=\"CSS\"> CSS <br>\n" +
                        "Selector Path: <input type=\"text\" name=\"selectorPath-faq_instance_link_1\" required><br>" +
                        "</fieldset>";
            } else {
                linkedFAQContainer1.innerHTML = "";
            }
        }

        function paginationToggle() {
            var paginationCheckbox = document.getElementById("paginationCheckbox");
            var paginationSelectorContainer = document.getElementById("paginationSelectorContainer");
            if (paginationCheckbox.checked) {
                paginationSelectorContainer.innerHTML =
                        "<fieldset>" +
                        "<legend>Pagination Link Selector</legend>" +
                        "Selector Type: <input type=\"radio\" name=\"selectorType-pagination_link\" value=\"XPath\" checked> XPath <input type=\"radio\" name=\"selectorType-pagination_link\" value=\"CSS\"> CSS <br>\n" +
                        "Selector Path: <input type=\"text\" name=\"selectorPath-pagination_link\" required><br>" +
                        "</fieldset>";
            } else {
                paginationSelectorContainer.innerHTML = "";
            }
        }

        function useSourceToggle() {
            var paginationCheckbox = document.getElementById("paginationCheckbox");
            var useSourceCheckbox = document.getElementById("useSourceCheckbox");
            var sourceInputContainer = document.getElementById("sourceInputContainer");
            var paginationFieldset = document.getElementById("paginationFieldset");
            var projectTypeSelector = document.getElementById("projectType");
            var projectTypeSelectionDiv = document.getElementById("typeSelection");
            if (useSourceCheckbox.checked) {
                paginationFieldset.style.display = "none";
                paginationCheckbox.checked = false;
                paginationSelectorContainer.innerHTML = "";
                projectTypeSelector.value="single-page";
                projectTypeToggle();
                projectTypeSelectionDiv.style.display = "none";
                sourceInputContainer.innerHTML =
                        "<textarea rows=\"20\" cols=\"60\" name=\"pageSource\" form=\"newProjectForm\"></textarea><br>";
            } else {
                paginationFieldset.style.display = "block";
                sourceInputContainer.innerHTML = "";
                projectTypeSelectionDiv.style.display = "block";

            }
        }
    </script>
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
    <form action="/newProject" method="post" id="newProjectForm">
    <div class="primaryCol col col-md-3">
        <fieldset>
            <legend>General</legend>
            Project Title: <input type="text" name="projectTitle" required autofocus><br>
            URL: <input type="url" name="projectURL" required><br>
            Manual HTML Source? <input type="checkbox" name="useSourceCheckbox" id="useSourceCheckbox" value="true" style="margin-bottom: 8px;" onchange="useSourceToggle();"><br>
            <div id="sourceInputContainer"></div>
            <div id="typeSelection">Type: <select name="projectType" id="projectType" onchange="projectTypeToggle();"><option value="single-page">Single Page</option><option value="single-level-menu">Single Level Menu</option><option value="two-level-menu">Double Level Menu</option></select></div>
        </fieldset>
        <fieldset id="paginationFieldset">
            <legend>Pagination</legend>
            Pagination is in use?  <input type="checkbox" name="paginationCheckbox" id="paginationCheckbox" value="true" onchange="paginationToggle();">
            <div id="paginationSelectorContainer"></div>

        </fieldset>
        <div id="FAQLinkContainer0"></div>

    </div>

    <div class="primaryCol col col-md-3">
                <div id="FAQLinkContainer1"></div>
                <fieldset>
                    <legend>FAQ Instance Selector</legend>
                    Selector Type: <input type="radio" name="selectorType-faq_instance_selector" value="XPath" checked> XPath <input type="radio" name="selectorType-faq_instance_selector" value="CSS"> CSS <br>
                    Selector Path: <input type="text" name="selectorPath-faq_instance_selector"><br>
                </fieldset>
                <fieldset>
                    <legend>Question Text Selector</legend>
                    Selector Type: <input type="radio" name="selectorType-question" value="XPath" checked> XPath <input type="radio" name="selectorType-question" value="CSS"> CSS <br>
                    Selector Path: <input type="text" name="selectorPath-question" required><br>
                </fieldset>
                <fieldset>
                    <legend>Answer Text Selector</legend>
                    Selector Type: <input type="radio" name="selectorType-answer" value="XPath" checked> XPath <input type="radio" name="selectorType-answer" value="CSS"> CSS <br>
                    Selector Path: <input type="text" name="selectorPath-answer" required><br>
                </fieldset>
   <input class="btn btn-success" type="submit" value="Add Project">


    </div>
    </form>

    <div class="primaryCol col col-md-4">
        <legend>Help</legend>
        <p><strong>Project Title -</strong> A logical name for users to reference the project by.</p>
        <p><strong>URL -</strong> The complete URL of the FAQ page to be scraped.</p>
        <p><strong>HTML Page Source -</strong> If the website to be scraped requires user interaction to load the FAQs, enable this option and
        paste the complete source code of the site after interaction is complete.</p>
        <p><strong>Type:</strong><br>
            <strong>Single Page -</strong> All of the questions and answers can be found on the page specified in <strong>URL</strong>.<br>
            <strong>Single Level Menu -</strong> Each question and answer is on it's own page with a link available on the page specified in <strong>URL</strong>.<br>
            <strong>Double Level Menu -</strong> As with <strong>Single Level Menu</strong> but with an additional level of menu.
        </p>
        <p><strong>Pagination -</strong> Enable this if the page features numerated pages of FAQs/links to FAQs.</p>
        <p><strong>Pagination Link Selector -</strong> Select either 'XPath' or 'CSS' and specify the selector for an 'a' element that acts as the next
        page button.</p>
        <p><strong>Top/Sub Menu Link Selector -</strong> Select either 'XPath' or 'CSS' and specify the selector for the 'a' elements that link to each
        question (or the next menu).</p>
        <p><strong>FAQ Instance Selector -</strong>  Select either 'XPath' or 'CSS' and specify the selector for the outer element that defines the instance
            of an FAQ.</p>
        <p><strong>Question/Answer Text Selector -</strong>  Select either 'XPath' or 'CSS' and specify the selector for the question/answer text.<br>
        <i>Remember to use <code>/text()</code> when using XPath and <code>::text</code> when using CSS to extract the text from the element.</i></p>




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