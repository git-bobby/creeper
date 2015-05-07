<%@ page language="java" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    
    <title>数据采集</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	
	<script type="text/javascript" src="js/jquery-1.7.2.js"></script>
	<script type="text/javascript" src="js/jquery.progressbar.js"></script>
	<script type="text/javascript" src="js/percent.js"></script>
		
  </head>
  
  <body>
    <center>
    	<a href="./creeper_start.do?acqid=50038174259562947006">开始</a> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    	<a href="./creeper_pause.do?acqid=50038174259562947006">暂停</a> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    	<a href="./creeper_stop.do?acqid=50038174259562947006">结束</a> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    	
    	<br><br><br><br>
    	
    	<span class="progressBar" id="pb1"></span>
    	<iframe src="./pgs_data.do" style="width: 1200px; height: 600px; border: 1px solid red;"></iframe>
    </center>
  </body>
</html>
