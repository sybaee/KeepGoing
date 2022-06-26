<!DOCTYPE html>
<html>

<head>
  <title>Operating</title>
  <meta charset="utf-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no" />
  <link rel="stylesheet" href="style.css" />
</head>

<?php
$host = 'localhost';
$user = 'root';
$pw = 'dnogQVE4tTGWCR';
$dbName = 'finst3';
$mysqli = new mysqli($host, $user, $pw, $dbName);

$company_id = $_GET["company_code"];

$queryCName = "SELECT company_name FROM company_info WHERE company_id=" . $company_id;
$cName = mysqli_query($mysqli, $queryCName);
$company_name = mysqli_fetch_assoc($cName)["company_name"];
?>

<body>
  <div>
    <form class="home-button" action="finst.html" method="post">
      <style>
        #homeFINST {
          background: #ffffff;
          color: #51A0D5;
          width: 150px;
          height: 60px;
          font-size: 1.5em;
          padding: 0.2em;
          text-align: center;
          border: none;
          position: relative;
          cursor: pointer;
          transition: 800ms ease all;
          outline: none;
        }

        #goBack button:hover {
          background: #ffffff;
          color: #51A0D5;
        }

        #goBack button:before,
        button:after {
          content: '';
          position: absolute;
          top: 0;
          right: 0;
          height: 2px;
          width: 0;
          background: #ffffff;
          transition: 400ms ease all;
        }

        #goBack button:after {
          right: inherit;
          top: inherit;
          left: 0;
          bottom: 0;
        }

        #goBack button:hover:before,
        button:hover:after {
          width: 100%;
          transition: 800ms ease all;
        }
      </style>

      <body>
        <div id="goBack" class='right-box'>
          <button id="homeFINST"> Home </button>
        </div>
      </body>
    </form>
  </div>

  <div class="container-index">
    <header class="hero-top">
      <!-- <span style="font-size: 2em; float:left;" class="highlight" >FINST</span> -->
      <h3>
        <span style="font-size: 2em; color: #51A0D5;" class="highlight"> <?php echo $company_name ?> </span>
      </h3>
      <h5>
        <span style="font-size: 1em;"> Financial Statement is All In </span>
      </h5>
    </header>

    <form class="finst-search">
      <style>
        #btnFINST {
          background: #51A0D5;
          color: #ffffff;
          width: 150px;
          height: 60px;
          font-size: 1.2em;
          padding: 0.2em;
          text-align: center;
          border: none;
          position: relative;
          cursor: pointer;
          transition: 800ms ease all;
          outline: none;
          margin-top: 6px;
          margin-right: 1.8px;
        }

        #btn_group button:hover {
          background: #ffffff;
          color: #51A0D5;
        }

        #btn_group button:before,
        button:after {
          content: '';
          position: absolute;
          top: 0;
          right: 0;
          height: 2px;
          width: 0;
          background: #51A0D5;
          transition: 400ms ease all;
        }

        #btn_group button:after {
          right: inherit;
          top: inherit;
          left: 0;
          bottom: 0;
        }

        #btn_group button:hover:before,
        button:hover:after {
          width: 100%;
          transition: 800ms ease all;
        }
      </style>

      <body>
        <div id="btn_group">
          <button type="button" id="btnFINST" onclick="javascript:location.href='./assets.php?company_code=<?= intval($company_id) ?>'">Assets </button>
          <button type="button" id="btnFINST" onclick="javascript:location.href='./capital.php?company_code=<?= intval($company_id) ?>'">Capital </button>
          <button type="button" id="btnFINST" onclick="javascript:location.href='./financial.php?company_code=<?= intval($company_id) ?>'">Financial </button>
          <button type="button" id="btnFINST" onclick="javascript:location.href='./indi.php?company_code=<?= intval($company_id) ?>'">Indi Cash Flow </button>
          <button type="button" id="btnFINST" onclick="javascript:location.href='./inte.php?company_code=<?= intval($company_id) ?>'">Inte Cash Flow </button>
          <button type="button" id="btnFINST" onclick="javascript:location.href='./liabilities.php?company_code=<?= intval($company_id) ?>'">Liabilities </button>
          <button type="button" id="btnFINST" onclick="javascript:location.href='./net.php?company_code=<?= intval($company_id) ?>'">Net Profit </button>
          <button type="button" id="btnFINST" onclick="javascript:location.href='./operating.php?company_code=<?= intval($company_id) ?>'">Operating </button>
          <button type="button" id="btnFINST" onclick="javascript:location.href='./other.php?company_code=<?= intval($company_id) ?>'">Other </button>
        </div>
      </body>
    </form>
  </div>
</body>



<?php
$queryCount = "SELECT COUNT(DISTINCT report_nm) AS NUM FROM fin_state_rcept_info WHERE company_id=" . $company_id;
$count = mysqli_query($mysqli, $queryCount);
$countNum = mysqli_fetch_assoc($count)["NUM"];

$queryReport = "SELECT report_nm FROM fin_state_rcept_info WHERE company_id={$company_id} ORDER BY report_nm ASC";
$report_nm = mysqli_query($mysqli, $queryReport);

$queryOperating = "SELECT report_nm, sales, cost_of_sales, gross_profit, selling_and_administ_rative_expenses, operating_income
                     FROM operating f
                     JOIN fin_state_dcm_info fsdi ON f.dcm_no = fsdi.dcm_no
                     JOIN fin_state_rcept_info fsri ON fsdi.rcept_no = fsri.rcept_no
                     JOIN company_info ci on fsri.company_id = ci.company_id AND ci.company_id={$company_id} ORDER BY report_nm ASC";
$operating = mysqli_query($mysqli, $queryOperating);

$rows = array();
$table = array();
$table['cols'] = array(
  array('label' => 'report_nm', 'type' => 'date'),
  array('label' => 'sales', 'type' => 'number'),
  array('label' => 'cost of sales', 'type' => 'number'),
  array('label' => 'gross profit', 'type' => 'number'),
  array('label' => 'selling and administ rative expenses', 'type' => 'number'),
  array('label' => 'operating income', 'type' => 'number'),
);

foreach ($operating as $r) {
  $temp = array();
  $dateString = $r['report_nm'];
  $dateArray = explode('.', $dateString);
  $year = $dateArray[0];
  $month = $dateArray[1] - 1; // subtract 1 to convert to javascript's 0-indexed months
  $day = 1;

  // The following line will be used to slice the Pie chart
  $temp[] = array('v' => "Date($year, $month, $day)");

  // Values of the each slice
  $temp[] = array('v' => (int) $r['sales']);
  $temp[] = array('v' => (int) $r['cost_of_sales']);
  $temp[] = array('v' => (int) $r['gross_profit']);
  $temp[] = array('v' => (int) $r['selling_and_administ_rative_expenses']);
  $temp[] = array('v' => (int) $r['operating_income']);
  $rows[] = array('c' => $temp);
}

$table['rows'] = $rows;

// echo "Debugging".$countNum;
$jsonTable = json_encode($table);
?>

</html>

<head>
  <meta charset="utf-8" />
  <title>Line_Controls_Chart</title>
  <!-- jQuery -->
  <script src="https://code.jquery.com/jquery.min.js"></script>
  <!-- google charts -->
  <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
</head>

<body>

  <body>
    <style type="text/css">
      .tg {
        border-collapse: collapse;
        border-color: #ccc;
        border-spacing: 0;
      }

      .tg td {
        background-color: #fff;
        border-color: #ccc;
        border-style: solid;
        border-width: 1px;
        color: #333;
        font-family: Arial, sans-serif;
        font-size: 14px;
        overflow: hidden;
        padding: 10px 5px;
        word-break: normal;
      }

      .tg th {
        background-color: #f0f0f0;
        border-color: #ccc;
        border-style: solid;
        border-width: 1px;
        color: #333;
        font-family: Arial, sans-serif;
        font-size: 14px;
        font-weight: normal;
        overflow: hidden;
        padding: 10px 5px;
        word-break: normal;
      }

      .tg .tg-wa1i {
        font-weight: bold;
        text-align: center;
        vertical-align: middle
      }

      .tg .tg-yla0 {
        font-weight: bold;
        text-align: left;
        vertical-align: middle
      }

      .tg .tg-ixqv {
        background-color: #f9f9f9;
        font-weight: bold;
        text-align: left;
        vertical-align: middle
      }

      .tg .tg-57iy {
        background-color: #f9f9f9;
        text-align: center;
        vertical-align: middle
      }

      .tg .tg-nrix {
        text-align: center;
        vertical-align: middle
      }
    </style>

    <div class="container-index" style="text-align: center;">
      <h4>
        <br /> <br />
        <span style="font-size: 1.5em;" color="#000000"> Operating </span>
        <br />
      </h4>
    </div>

    <div class="container-index">
      <header class="hero-top">
        <h4>
          <br /> <br />
          <span style="font-size: 1.2em;" color="#000000"> Table </span>
          <br /> <br />
        </h4>
      </header>
    </div>
    <div style="width: 75.6%; margin:0 auto; overflow-x:scroll ;text-align:center">
      <table class="tg">
        <thead>
          <tr>
            <th class="tg-yla0"> </th>
            <?php
            mysqli_data_seek($report_nm, 0);
            while ($rowReport = mysqli_fetch_assoc($report_nm)) {
              echo "<th class=\"tg-wa1i\">", $rowReport["report_nm"], "</th>";
            }
            ?>
          </tr>
        </thead>
        <tbody>
          <tr>
            <td class="tg-yla0">Sales</td>
            <?php
            mysqli_data_seek($operating, 0);
            while ($rowOperating = mysqli_fetch_assoc($operating)) {
              $money = number_format($rowOperating["sales"]);
              echo "<td class=\"tg-nrix\">", $money, "</td>";
            }
            ?>
          </tr>
          <tr>
            <td class="tg-ixqv">Cost of Sales</td>
            <?php
            mysqli_data_seek($operating, 0);
            while ($rowOperating = mysqli_fetch_assoc($operating)) {
              $money = number_format($rowOperating["cost_of_sales"]);
              echo "<td class=\"tg-57iy\">", $money, "</td>";
            }
            ?>
          </tr>
          <tr>
            <td class="tg-yla0">Gross Profit</td>
            <?php
            mysqli_data_seek($operating, 0);
            while ($rowOperating = mysqli_fetch_assoc($operating)) {
              $money = number_format($rowOperating["gross_profit"]);
              echo "<td class=\"tg-nrix\">", $money, "</td>";
            }
            ?>
          </tr>
          <tr>
            <td class="tg-ixqv">Selling and Administ Rative Expenses</td>
            <?php
            mysqli_data_seek($operating, 0);
            while ($rowOperating = mysqli_fetch_assoc($operating)) {
              $money = number_format($rowOperating["selling_and_administ_rative_expenses"]);
              echo "<td class=\"tg-57iy\">", $money, "</td>";
            }
            ?>
          </tr>
          <tr>
            <td class="tg-yla0">Operating Income</td>
            <?php
            mysqli_data_seek($operating, 0);
            while ($rowOperating = mysqli_fetch_assoc($operating)) {
              $money = number_format($rowOperating["operating_income"]);
              echo "<td class=\"tg-nrix\">", $money, "</td>";
            }
            ?>
          </tr>
        </tbody>
      </table>

    </div>

    <div class="container-index">
      <header class="hero-top">
        <h4>
          <br /> <br />
          <span style="font-size: 1.2em;" color="#000000"> Graph </span>
        </h4>
      </header>
    </div>

    <div id="Line_Controls_Chart">
      <div id="lineChartArea" style="padding: 0px 20px 0px 0px;"></div>
      <div id="controlsArea" style="padding: 0px 20px 0px 0px;"></div>
    </div>
  </body>

  <script>
    var chartDrowFun = {
      chartDrow: function() {
        var chartData = "";
        var chartDateformat = "yyyy.MM";
        var chartLineCount = 10;
        var controlLineCount = 10;

        function drawDashboard() {
          var data = new google.visualization.DataTable(<?= $jsonTable ?>);
          var chart = new google.visualization.ChartWrapper({
            chartType: "LineChart",
            containerId: "lineChartArea",
            options: {
              isStacked: "percent",
              focusTarget: "category",
              height: 500,
              width: "100%",
              legend: {
                position: "top",
                textStyle: {
                  fontSize: 13
                }
              },
              pointSize: 5,
              tooltip: {
                textStyle: {
                  fontSize: 12
                },
                showColorCode: true,
                trigger: "both",
              },
              hAxis: {
                format: chartDateformat,
                gridlines: {
                  count: chartLineCount,
                  units: {
                    years: {
                      format: ["yyyy"]
                    },
                    months: {
                      format: ["MM"]
                    },
                  },
                },
                textStyle: {
                  fontSize: 12
                },
              },
              vAxis: {
                minValue: 0,
                gridlines: {
                  count: -1
                },
                textStyle: {
                  fontSize: 12
                },
              },
              animation: {
                startup: true,
                duration: 1000,
                easing: "in"
              },
              annotations: {
                pattern: chartDateformat,
                textStyle: {
                  fontSize: 15,
                  bold: true,
                  italic: true,
                  color: "#871b47",
                  auraColor: "#d799ae",
                  opacity: 0.8,
                  pattern: chartDateformat,
                },
              },
            },
          });

          var control = new google.visualization.ControlWrapper({
            controlType: "ChartRangeFilter",
            containerId: "controlsArea",
            options: {
              ui: {
                chartType: "LineChart",
                chartOptions: {
                  chartArea: {
                    width: "60%",
                    height: 80
                  },
                  hAxis: {
                    baselineColor: "none",
                    format: chartDateformat,
                    textStyle: {
                      fontSize: 12
                    },
                    gridlines: {
                      count: controlLineCount,
                      units: {
                        years: {
                          format: ["yyyy"]
                        },
                        months: {
                          format: ["MM"]
                        },

                      },
                    },
                  },
                },
              },
              filterColumnIndex: 0,
            },
          });

          var date_formatter = new google.visualization.DateFormat({
            pattern: chartDateformat,
          });
          date_formatter.format(data, 0);

          var dashboard = new google.visualization.Dashboard(
            document.getElementById("Line_Controls_Chart")
          );
          window.addEventListener(
            "resize",
            function() {
              dashboard.draw(data);
            },
            false
          ); // Change graph size according to screen size
          dashboard.bind([control], [chart]);
          dashboard.draw(data);
        }
        google.charts.setOnLoadCallback(drawDashboard);
      },
    };

    $(document).ready(function() {
      google.charts.load("current", {
        packages: ["line", "controls"]
      });
      chartDrowFun.chartDrow(); // chartDrow()
    });
  </script>

  <script>
    function onlyNumber() {
      if (event.keyCode < 48 || event.keyCode > 57) event.returnValue = false;
    }

    function maxLengthCheck(object) {
      if (object.value.length > object.maxLength) {
        object.value = object.value.slice(0, object.maxLength);
      }
    }

    function tickerTransmitter(object) {
      let inputTickerCode = document.getElementById("inputTickerCode");
      let selectedCode = object.getElementsByClassName("ticker-code")[0]
        .innerHTML;

      inputTickerCode.value = selectedCode;
    }
  </script>
</body>

</html>