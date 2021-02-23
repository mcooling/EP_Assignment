<%--@elvariable id="customers" type="model_beans.Customer"--%>

<%-- manually compiles json structure --%>

{
    headings: ["ID", "First Name", "Last Name", "Balance"],
    customers: [
        [
            "${ customers[0].customerID }",
            "${ customers[0].firstName }",
            "${ customers[0].lastName }",
            "${ customers[0].formattedBalance }"
        ],
        [
            "${ customers[1].customerID }",
            "${ customers[1].firstName }",
            "${ customers[1].lastName }",
            "${ customers[1].formattedBalance }"
        ]
    ]
}
