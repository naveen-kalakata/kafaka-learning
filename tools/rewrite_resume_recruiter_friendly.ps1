Add-Type -AssemblyName System.IO.Compression
Add-Type -AssemblyName System.IO.Compression.FileSystem

$sourcePath = 'C:\Users\navee\OneDrive\Desktop\Resume\Naveen_Kalakata_Base_Improved.docx'
$outputPath = 'C:\Users\navee\OneDrive\Desktop\Kafka-Learning\Naveen_Kalakata_Recruiter_Version.docx'
$sectPr = '<w:sectPr w:rsidR="005B7B8C" w:rsidSect="00034616"><w:pgSz w:w="12240" w:h="15840"/><w:pgMar w:top="792" w:right="936" w:bottom="792" w:left="936" w:header="720" w:footer="720" w:gutter="0"/><w:cols w:space="720"/><w:docGrid w:linePitch="360"/></w:sectPr>'

function Escape-XmlText {
    param([string]$Text)

    if ($null -eq $Text) {
        return ''
    }

    return [System.Security.SecurityElement]::Escape($Text)
}

function New-Run {
    param(
        [string]$Text,
        [switch]$Bold
    )

    $runProps = ''
    if ($Bold) {
        $runProps = '<w:rPr><w:b/></w:rPr>'
    }

    return "<w:r>$runProps<w:t xml:space=`"preserve`">$(Escape-XmlText $Text)</w:t></w:r>"
}

function New-Paragraph {
    param(
        [string[]]$Runs,
        [string]$Style = '',
        [string]$Justification = '',
        [int]$Before = 0,
        [int]$After = 120
    )

    $paragraphProps = @()
    if ($Style) {
        $paragraphProps += "<w:pStyle w:val=`"$Style`"/>"
    }
    if ($Justification) {
        $paragraphProps += "<w:jc w:val=`"$Justification`"/>"
    }
    $paragraphProps += "<w:spacing w:before=`"$Before`" w:after=`"$After`"/>"
    $propsXml = "<w:pPr>$($paragraphProps -join '')</w:pPr>"

    return "<w:p>$propsXml$($Runs -join '')</w:p>"
}

$paragraphs = @()
$paragraphs += New-Paragraph -Runs @(New-Run -Text 'NAVEEN KALAKATA' -Bold) -Style 'Title' -Justification 'center' -Before 0 -After 60
$paragraphs += New-Paragraph -Runs @(New-Run -Text 'Java Backend Engineer | Spring Boot | Microservices | Kafka | Oracle | AWS') -Style 'Subtitle' -Justification 'center' -Before 0 -After 40
$paragraphs += New-Paragraph -Runs @(New-Run -Text 'Frisco, TX | Open to Relocation | +1 (702) 296-6824 | naveenhy567@gmail.com | linkedin.com/in/naveen-kalakata') -Justification 'center' -Before 0 -After 180

$paragraphs += New-Paragraph -Runs @(New-Run -Text 'SUMMARY') -Style 'Heading1' -Before 120 -After 60
$paragraphs += New-Paragraph -Runs @(New-Run -Text 'Java backend engineer with 4+ years of experience building and supporting scalable backend systems in banking and enterprise environments. Strong in Java 17, Spring Boot, Kafka/JMS, Oracle, AWS, Docker, Kubernetes, Jenkins, and SQL performance tuning. At Bank of America, improved ECM API latency 87%, from 5 seconds to under 660 milliseconds, by optimizing MyBatis and Oracle queries for live deal workflows. Experienced across feature delivery, production support, CI/CD, and cross-functional Agile teams.') -After 140

$paragraphs += New-Paragraph -Runs @(New-Run -Text 'CORE SKILLS') -Style 'Heading1' -Before 120 -After 60
$paragraphs += New-Paragraph -Runs @(New-Run -Text 'Java 17, Spring Boot, Spring MVC, Microservices, REST APIs, Kafka, JMS, SOAP/WSDL, MyBatis, Hibernate, Oracle, MySQL, MongoDB, AWS, Docker, Kubernetes, Jenkins, Git, Splunk, JUnit, Linux, Agile/Scrum') -After 140

$paragraphs += New-Paragraph -Runs @(New-Run -Text 'PROFESSIONAL EXPERIENCE') -Style 'Heading1' -Before 120 -After 60
$paragraphs += New-Paragraph -Runs @((New-Run -Text 'Bank of America (via Randstad Digital) | Java Developer | Jan 2025 - Present' -Bold)) -After 40
$paragraphs += New-Paragraph -Runs @(New-Run -Text 'Build and support Java/Spring MVC backend applications for Equity Capital Markets deal execution workflows in a high-availability production environment.') -Style 'ListBullet' -After 20
$paragraphs += New-Paragraph -Runs @(New-Run -Text 'Improved Masterbook API response time 87%, from 5 seconds to under 660 milliseconds, by optimizing MyBatis XML queries and Oracle access patterns.') -Style 'ListBullet' -After 20
$paragraphs += New-Paragraph -Runs @(New-Run -Text 'Integrated SOAP/WSDL services and JMS messaging with Ipreo and Dealogic to support Bookbuild and Deal Linking workflows.') -Style 'ListBullet' -After 20
$paragraphs += New-Paragraph -Runs @(New-Run -Text 'Investigate production incidents in Splunk, perform root cause analysis, and maintain system reliability for live deal operations.') -Style 'ListBullet' -After 20
$paragraphs += New-Paragraph -Runs @(New-Run -Text 'Support platform security and uptime through certificate lifecycle management and day-to-day production support.') -Style 'ListBullet' -After 100

$paragraphs += New-Paragraph -Runs @((New-Run -Text 'Tech Club Inc | Java Developer Intern | Jul 2024 - Nov 2024' -Bold)) -After 40
$paragraphs += New-Paragraph -Runs @(New-Run -Text 'Developed Java 17 and Spring Boot REST APIs and microservices to improve backend modularity and reduce inter-service coupling.') -Style 'ListBullet' -After 20
$paragraphs += New-Paragraph -Runs @(New-Run -Text 'Used records and sealed classes to simplify API contracts and domain modeling for CRUD workflows.') -Style 'ListBullet' -After 20
$paragraphs += New-Paragraph -Runs @(New-Run -Text 'Containerized services with Docker to reduce environment drift across development and staging.') -Style 'ListBullet' -After 20
$paragraphs += New-Paragraph -Runs @(New-Run -Text 'Built Jenkins CI/CD pipelines, added JUnit test coverage, and contributed through code reviews and Agile sprint delivery.') -Style 'ListBullet' -After 100

$paragraphs += New-Paragraph -Runs @((New-Run -Text 'Cognizant | Java Developer | Jan 2021 - Jul 2022' -Bold)) -After 40
$paragraphs += New-Paragraph -Runs @(New-Run -Text 'Developed backend features for SAP Commerce (Hybris) using Java, Spring MVC, Maven, and MySQL.') -Style 'ListBullet' -After 20
$paragraphs += New-Paragraph -Runs @(New-Run -Text 'Customized catalog, order, and inventory workflows for client-specific e-commerce requirements.') -Style 'ListBullet' -After 20
$paragraphs += New-Paragraph -Runs @(New-Run -Text 'Optimized MySQL queries and resolved order placement failures during Tier-3 production support.') -Style 'ListBullet' -After 20
$paragraphs += New-Paragraph -Runs @(New-Run -Text 'Worked with cross-functional teams to troubleshoot backend issues and improve platform stability during peak traffic periods.') -Style 'ListBullet' -After 100

$paragraphs += New-Paragraph -Runs @(New-Run -Text 'PROJECTS') -Style 'Heading1' -Before 120 -After 60
$paragraphs += New-Paragraph -Runs @((New-Run -Text 'E-commerce Web Application | Python, Flask, AWS' -Bold)) -After 40
$paragraphs += New-Paragraph -Runs @(New-Run -Text 'Deployed a full-stack e-commerce application on AWS using EC2, RDS, S3, SES, and VPC components.') -Style 'ListBullet' -After 20
$paragraphs += New-Paragraph -Runs @(New-Run -Text 'Built storefront, cart, and checkout workflows with Flask, JavaScript, Bootstrap, and responsive UI patterns.') -Style 'ListBullet' -After 20
$paragraphs += New-Paragraph -Runs @(New-Run -Text 'Applied IAM roles and security groups to enforce least-privilege access in a production-style environment.') -Style 'ListBullet' -After 100

$paragraphs += New-Paragraph -Runs @((New-Run -Text 'Book E-Shopping | Java, Spring Boot, MySQL' -Bold)) -After 40
$paragraphs += New-Paragraph -Runs @(New-Run -Text 'Built an online bookstore with registration, JWT authentication, rental and purchase flows, and admin inventory management.') -Style 'ListBullet' -After 20
$paragraphs += New-Paragraph -Runs @(New-Run -Text 'Developed end-to-end backend and server-rendered workflows using Spring Boot, JSP, and MySQL.') -Style 'ListBullet' -After 100

$paragraphs += New-Paragraph -Runs @(New-Run -Text 'EDUCATION') -Style 'Heading1' -Before 120 -After 60
$paragraphs += New-Paragraph -Runs @((New-Run -Text 'University of Central Missouri | Master of Science in Computer Science | Aug 2022 - May 2024' -Bold)) -After 20
$paragraphs += New-Paragraph -Runs @(New-Run -Text 'CGPA: 3.4') -After 60
$paragraphs += New-Paragraph -Runs @((New-Run -Text 'Amrita Vishwa Vidyapeetham | Bachelor of Technology in Computer Science | Jul 2017 - May 2021' -Bold)) -After 100

$paragraphs += New-Paragraph -Runs @(New-Run -Text 'CERTIFICATIONS') -Style 'Heading1' -Before 120 -After 60
$paragraphs += New-Paragraph -Runs @(New-Run -Text 'AWS Academy Cloud Foundations (2023) | AWS Academy Cloud Architecting (2023) | Oracle Java Explorer (2022) | Git from Scratch, LinkedIn Learning (2022)') -After 0

$documentXml = @"
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<w:document xmlns:w="http://schemas.openxmlformats.org/wordprocessingml/2006/main">
  <w:body>
    $($paragraphs -join "`r`n    ")
    $sectPr
  </w:body>
</w:document>
"@

Copy-Item -LiteralPath $sourcePath -Destination $outputPath -Force

$archive = [System.IO.Compression.ZipFile]::Open($outputPath, [System.IO.Compression.ZipArchiveMode]::Update)
try {
    $existingEntry = $archive.GetEntry('word/document.xml')
    if ($existingEntry) {
        $existingEntry.Delete()
    }

    $newEntry = $archive.CreateEntry('word/document.xml')
    $stream = $newEntry.Open()
    $writer = New-Object System.IO.StreamWriter($stream, (New-Object System.Text.UTF8Encoding($false)))
    $writer.Write($documentXml)
    $writer.Dispose()
}
finally {
    $archive.Dispose()
}

Write-Output $outputPath
