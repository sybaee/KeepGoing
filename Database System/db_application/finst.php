<!DOCTYPE html>
<html>

<head>
  <title>Finst ALL</title>
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

  $company_id = $_POST['company_code'];

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

$queryAssets = "SELECT report_nm, current_assets, non_current_assets, total_assets 
                FROM assets a
                JOIN fin_state_dcm_info fsdi ON a.dcm_no = fsdi.dcm_no
                JOIN fin_state_rcept_info fsri ON fsdi.rcept_no = fsri.rcept_no
                JOIN company_info ci on fsri.company_id = ci.company_id AND ci.company_id={$company_id} ORDER BY report_nm ASC";
$assets = mysqli_query($mysqli, $queryAssets);

$queryCapital = "SELECT report_nm, equity_capital, capital_contributed_in_excess_of_par, retained_earnings, other_capital_item, total_capital, total_capital_and_liabilities
                 FROM capital c
                 JOIN fin_state_dcm_info fsdi ON c.dcm_no = fsdi.dcm_no
                 JOIN fin_state_rcept_info fsri ON fsdi.rcept_no = fsri.rcept_no
                 JOIN company_info ci on fsri.company_id = ci.company_id AND ci.company_id={$company_id} ORDER BY report_nm ASC";
$capital = mysqli_query($mysqli, $queryCapital);

$queryFinancial = "SELECT report_nm, financial_income, financial_expenses
                   FROM financial f
                   JOIN fin_state_dcm_info fsdi ON f.dcm_no = fsdi.dcm_no
                   JOIN fin_state_rcept_info fsri ON fsdi.rcept_no = fsri.rcept_no
                   JOIN company_info ci on fsri.company_id = ci.company_id AND ci.company_id={$company_id} ORDER BY report_nm ASC";
$financial = mysqli_query($mysqli, $queryFinancial);

$queryIndividual = "SELECT report_nm, cash_flows_from_operating_activities, cash_flows_from_investing_activities, cash_flows_from_financing_activities, cash_flow_from_foreign_currency_translation
                    FROM individual_cash_flow f
                    JOIN fin_state_dcm_info fsdi ON f.dcm_no = fsdi.dcm_no
                    JOIN fin_state_rcept_info fsri ON fsdi.rcept_no = fsri.rcept_no
                    JOIN company_info ci on fsri.company_id = ci.company_id AND ci.company_id={$company_id} ORDER BY report_nm ASC";
$individual = mysqli_query($mysqli, $queryIndividual);

$queryIntegrated = "SELECT report_nm, net_increase_cash_and_cash_equivalents, cash_and_cash_equivalents_at_beginning_of_period, cash_and_cash_equivalents_at_end_of_period
                    FROM integrated_cash_flow f
                    JOIN fin_state_dcm_info fsdi ON f.dcm_no = fsdi.dcm_no
                    JOIN fin_state_rcept_info fsri ON fsdi.rcept_no = fsri.rcept_no
                    JOIN company_info ci on fsri.company_id = ci.company_id AND ci.company_id={$company_id} ORDER BY report_nm ASC";
$integrated = mysqli_query($mysqli, $queryIntegrated);

$queryLiabilities = "SELECT report_nm, liabilities_assets, non_liabilities_assets, total_liabilities
                     FROM liabilities f
                     JOIN fin_state_dcm_info fsdi ON f.dcm_no = fsdi.dcm_no
                     JOIN fin_state_rcept_info fsri ON fsdi.rcept_no = fsri.rcept_no
                     JOIN company_info ci on fsri.company_id = ci.company_id AND ci.company_id={$company_id} ORDER BY report_nm ASC";
$liabilities = mysqli_query($mysqli, $queryLiabilities);

$queryNet = "SELECT report_nm, income_before_income_taxes, income_tax_expenses, net_income, earning_per_share
             FROM net_profit f
             JOIN fin_state_dcm_info fsdi ON f.dcm_no = fsdi.dcm_no
             JOIN fin_state_rcept_info fsri ON fsdi.rcept_no = fsri.rcept_no
             JOIN company_info ci on fsri.company_id = ci.company_id AND ci.company_id={$company_id} ORDER BY report_nm ASC";
$net = mysqli_query($mysqli, $queryNet);

$queryOperating = "SELECT report_nm, sales, cost_of_sales, gross_profit, selling_and_administ_rative_expenses, operating_income
                     FROM operating f
                     JOIN fin_state_dcm_info fsdi ON f.dcm_no = fsdi.dcm_no
                     JOIN fin_state_rcept_info fsri ON fsdi.rcept_no = fsri.rcept_no
                     JOIN company_info ci on fsri.company_id = ci.company_id AND ci.company_id={$company_id} ORDER BY report_nm ASC";
$operating = mysqli_query($mysqli, $queryOperating);

$queryOther = "SELECT report_nm, other_income, other_expenses
               FROM other f
               JOIN fin_state_dcm_info fsdi ON f.dcm_no = fsdi.dcm_no
               JOIN fin_state_rcept_info fsri ON fsdi.rcept_no = fsri.rcept_no
               JOIN company_info ci on fsri.company_id = ci.company_id AND ci.company_id={$company_id} ORDER BY report_nm ASC";
$other = mysqli_query($mysqli, $queryOther);

// echo "Debugging".$countNum;
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
        <span style="font-size: 1.5em;" color="#000000"> All Values </span>
        <br /> <br /> <br />
      </h4>
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
            <td class="tg-ixqv">Current Assets</td>
            <?php
            mysqli_data_seek($assets, 0);
            while ($rowAssets = mysqli_fetch_assoc($assets)) {
              $money = number_format($rowAssets["current_assets"]);
              echo "<td class=\"tg-57iy\">", $money, "</td>";
            }
            ?>
          </tr>
          <tr>
            <td class="tg-yla0">Non-current Assets</td>
            <?php
            mysqli_data_seek($assets, 0);
            while ($rowAssets = mysqli_fetch_assoc($assets)) {
              $money = number_format($rowAssets["non_current_assets"]);
              echo "<td class=\"tg-nrix\">", $money, "</td>";
            }
            ?>
          </tr>
          <tr>
            <td class="tg-ixqv">Total Assets</td>
            <?php
            mysqli_data_seek($assets, 0);
            while ($rowAssets = mysqli_fetch_assoc($assets)) {
              $money = number_format($rowAssets["total_assets"]);
              echo "<td class=\"tg-57iy\">", $money, "</td>";
            }
            ?>
          </tr>
          <tr>
            <td class="tg-yla0">Equity Capital</td>
            <?php
            mysqli_data_seek($capital, 0);
            while ($rowCapital = mysqli_fetch_assoc($capital)) {
              $money = number_format($rowCapital["equity_capital"]);
              echo "<td class=\"tg-nrix\">", $money, "</td>";
            }
            ?>
          </tr>
          <tr>
            <td class="tg-ixqv">Capital Contributed in Excess of Par</td>
            <?php
            mysqli_data_seek($capital, 0);
            while ($rowCapital = mysqli_fetch_assoc($capital)) {
              $money = number_format($rowCapital["capital_contributed_in_excess_of_par"]);
              echo "<td class=\"tg-57iy\">", $money, "</td>";
            }
            ?>
          </tr>
          <tr>
            <td class="tg-yla0">Retained Earnigs</td>
            <?php
            mysqli_data_seek($capital, 0);
            while ($rowCapital = mysqli_fetch_assoc($capital)) {
              $money = number_format($rowCapital["retained_earnings"]);
              echo "<td class=\"tg-nrix\">", $money, "</td>";
            }
            ?>
          </tr>
          <tr>
            <td class="tg-ixqv">Other Capital Item</td>
            <?php
            mysqli_data_seek($capital, 0);
            while ($rowCapital = mysqli_fetch_assoc($capital)) {
              $money = number_format($rowCapital["other_capital_item"]);
              echo "<td class=\"tg-57iy\">", $money, "</td>";
            }
            ?>
          </tr>
          <tr>
            <td class="tg-yla0">Total Capital</td>
            <?php
            mysqli_data_seek($capital, 0);
            while ($rowCapital = mysqli_fetch_assoc($capital)) {
              $money = number_format($rowCapital["total_capital"]);
              echo "<td class=\"tg-nrix\">", $money, "</td>";
            }
            ?>
          </tr>
          <tr>
            <td class="tg-ixqv">Total Capital and Liabilities</td>
            <?php
            mysqli_data_seek($capital, 0);
            while ($rowCapital = mysqli_fetch_assoc($capital)) {
              $money = number_format($rowCapital["total_capital_and_liabilities"]);
              echo "<td class=\"tg-57iy\">", $money, "</td>";
            }
            ?>
          </tr>
          <tr>
            <td class="tg-yla0">Financial Income</td>
            <?php
            mysqli_data_seek($financial, 0);
            while ($rowFinancial = mysqli_fetch_assoc($financial)) {
              $money = number_format($rowFinancial["financial_income"]);
              echo "<td class=\"tg-nrix\">", $money, "</td>";
            }
            ?>
          </tr>
          <tr>
            <td class="tg-ixqv">Financial Expenses</td>
            <?php
            mysqli_data_seek($financial, 0);
            while ($rowFinancial = mysqli_fetch_assoc($financial)) {
              $money = number_format($rowFinancial["financial_expenses"]);
              echo "<td class=\"tg-57iy\">", $money, "</td>";
            }
            ?>
          </tr>
          <tr>
            <td class="tg-yla0">Cash Flows from Operating Activities</td>
            <?php
            mysqli_data_seek($individual, 0);
            while ($rowIndividual = mysqli_fetch_assoc($individual)) {
              $money = number_format($rowIndividual["cash_flows_from_operating_activities"]);
              echo "<td class=\"tg-nrix\">", $money, "</td>";
            }
            ?>

          </tr>
          <tr>
            <td class="tg-ixqv">Cash Flows from Investing Activities</td>
            <?php
            mysqli_data_seek($individual, 0);
            while ($rowIndividual = mysqli_fetch_assoc($individual)) {
              $money = number_format($rowIndividual["cash_flows_from_investing_activities"]);
              echo "<td class=\"tg-57iy\">", $money, "</td>";
            }
            ?>
          </tr>
          <tr>
            <td class="tg-yla0">Cash Flows from Financing Activities</td>
            <?php
            mysqli_data_seek($individual, 0);
            while ($rowIndividual = mysqli_fetch_assoc($individual)) {
              $money = number_format($rowIndividual["cash_flows_from_financing_activities"]);
              echo "<td class=\"tg-nrix\">", $money, "</td>";
            }
            ?>

          </tr>
          <tr>
            <td class="tg-ixqv">Cash Flows from Foreign Currency Translation</td>
            <?php
            mysqli_data_seek($individual, 0);
            while ($rowIndividual = mysqli_fetch_assoc($individual)) {
              $money = number_format($rowIndividual["cash_flow_from_foreign_currency_translation"]);
              echo "<td class=\"tg-57iy\">", $money, "</td>";
            }
            ?>
          </tr>
          <tr>
            <td class="tg-yla0">Net Increase Cash and Cash Equivalents</td>
            <?php
            mysqli_data_seek($integrated, 0);
            while ($rowIntegrated = mysqli_fetch_assoc($integrated)) {
              $money = number_format($rowIntegrated["net_increase_cash_and_cash_equivalents"]);
              echo "<td class=\"tg-nrix\">", $money, "</td>";
            }
            ?>
          </tr>
          <tr>
            <td class="tg-ixqv">Cash and Cash Equivalents at Beginning of Period</td>
            <?php
            mysqli_data_seek($integrated, 0);
            while ($rowIntegrated = mysqli_fetch_assoc($integrated)) {
              $money = number_format($rowIntegrated["cash_and_cash_equivalents_at_beginning_of_period"]);
              echo "<td class=\"tg-57iy\">", $money, "</td>";
            }
            ?>
          </tr>
          <tr>
            <td class="tg-yla0">Cash and Cash Equivalents at End of Period</td>
            <?php
            mysqli_data_seek($integrated, 0);
            while ($rowIntegrated = mysqli_fetch_assoc($integrated)) {
              $money = number_format($rowIntegrated["cash_and_cash_equivalents_at_end_of_period"]);
              echo "<td class=\"tg-nrix\">", $money, "</td>";
            }
            ?>
          </tr>
          <tr>
            <td class="tg-ixqv">Liabilities Assets</td>
            <?php
            mysqli_data_seek($liabilities, 0);
            while ($rowLiabilities = mysqli_fetch_assoc($liabilities)) {
              $money = number_format($rowLiabilities["liabilities_assets"]);
              echo "<td class=\"tg-57iy\">", $money, "</td>";
            }
            ?>
          </tr>
          <tr>
            <td class="tg-yla0">Non-liabilities Assets</td>
            <?php
            mysqli_data_seek($liabilities, 0);
            while ($rowLiabilities = mysqli_fetch_assoc($liabilities)) {
              $money = number_format($rowLiabilities["non_liabilities_assets"]);
              echo "<td class=\"tg-nrix\">", $money, "</td>";
            }
            ?>
          </tr>
          <tr>
            <td class="tg-ixqv">Total Liabilities</td>
            <?php
            mysqli_data_seek($liabilities, 0);
            while ($rowLiabilities = mysqli_fetch_assoc($liabilities)) {
              $money = number_format($rowLiabilities["total_liabilities"]);
              echo "<td class=\"tg-57iy\">", $money, "</td>";
            }
            ?>
          </tr>
          <tr>
            <td class="tg-yla0">Income before Income Taxes</td>
            <?php
            mysqli_data_seek($net, 0);
            while ($rowNet = mysqli_fetch_assoc($net)) {
              $money = number_format($rowNet["income_before_income_taxes"]);
              echo "<td class=\"tg-nrix\">", $money, "</td>";
            }
            ?>
          </tr>
          <tr>
            <td class="tg-ixqv">Income Tax Expenses</td>
            <?php
            mysqli_data_seek($net, 0);
            while ($rowNet = mysqli_fetch_assoc($net)) {
              $money = number_format($rowNet["income_tax_expenses"]);
              echo "<td class=\"tg-57iy\">", $money, "</td>";
            }
            ?>
          </tr>
          <tr>
            <td class="tg-yla0">Net Income</td>
            <?php
            mysqli_data_seek($net, 0);
            while ($rowNet = mysqli_fetch_assoc($net)) {
              $money = number_format($rowNet["net_income"]);
              echo "<td class=\"tg-nrix\">", $money, "</td>";
            }
            ?>
          </tr>
          <tr>
            <td class="tg-ixqv">Earning per Share</td>
            <?php
            mysqli_data_seek($net, 0);
            while ($rowNet = mysqli_fetch_assoc($net)) {
              $money = number_format($rowNet["earning_per_share"]);
              echo "<td class=\"tg-57iy\">", $money, "</td>";
            }
            ?>
          </tr>
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
          <tr>
            <td class="tg-ixqv">Other Income</td>
            <?php
            mysqli_data_seek($other, 0);
            while ($rowOther = mysqli_fetch_assoc($other)) {
              $money = number_format($rowOther["other_income"]);
              echo "<td class=\"tg-57iy\">", $money, "</td>";
            }
            ?>
          </tr>
          <tr>
            <td class="tg-yla0">Other Expenses</td>
            <?php
            mysqli_data_seek($other, 0);
            while ($rowOther = mysqli_fetch_assoc($other)) {
              $money = number_format($rowOther["other_expenses"]);
              echo "<td class=\"tg-nrix\">", $money, "</td>";
            }
            ?>
          </tr>
        </tbody>
      </table>

    </div>

    <div id="Line_Controls_Chart">
      <div id="lineChartArea" style="padding: 0px 20px 0px 0px;"></div>
      <div id="controlsArea" style="padding: 0px 20px 0px 0px;"></div>
    </div>

    <div class="container-index" style="text-align: right;">
      <header class="hero-top">
        <h3>
          <span style="font-size: 1.5em; color: #90C1D7;" class="highlight"> <br /> Contact Us <br /> </span>
        </h3>

        <h5>
          <span style="font-size: 1.2em;"> Handong Global University <br /> </span>
          <span style="font-size: 1em;"> Chanyeong Lee: 21400604@handong.edu <br /> </span>
          <span style="font-size: 1em;"> Seungye Bae: 21600326@handong.edu <br /> </span>
          <span style="font-size: 1em;"> Sujin Lim: 21600590@handong.edu <br /> </span>
          <span style="font-size: 1em;"> Harim Song: 21700386@handong.edu <br /> <br /> </span>
        </h5>
      </header>
    </div>
  </body>
  
</html>