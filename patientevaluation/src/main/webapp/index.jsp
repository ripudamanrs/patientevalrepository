<html>
<body>
	<h2>Patient Evaluation Concept</h2>
	<br>
	<form method="POST" action="rest">
		<table>
			<tr>
				<td>Enter ICD10-CM code: </td> 
				<td><input name="icd10cm" id="icd10cm" type="text"></td>
			</tr>
			<tr>
				<td>Enter path of patient CSV: </td>
				<td><input name="patientcsv" id="patientcsv" type="text"></td>
			</tr>
			<tr>
				<td>
					<input id="submitdetails" type="submit">
				</td>
			</tr>
		</table>
	</form>
</body>
</html>
