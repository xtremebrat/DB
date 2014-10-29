select distinct extractvalue(r.REVIEWXML, '/REVIEWS/Review/Reviewer') as "Reviewer"
from Book b, Review r
where extractvalue(r.REVIEWXML, '/REVIEWS/Review/Book_Title/text()') = extractvalue(b.BOOKXML, '/BOOKS/Book/Title/text()')
and extractvalue(b.BOOKXML, '/BOOKS/Book/Price/text()') >= '$25'
and extractvalue(b.BOOKXML, '/BOOKS/Book/Publish_Date/text()') not like '%-01-%'
and extractvalue(b.BOOKXML, '/BOOKS/Book/Publish_Date/text()') not like '%-02-%'
and extractvalue(b.BOOKXML, '/BOOKS/Book/Publish_Date/text()') not like '%-03-%'
and extractvalue(b.BOOKXML, '/BOOKS/Book/Publish_Date/text()') not like '%-04-%'
and extractvalue(b.BOOKXML, '/BOOKS/Book/Publish_Date/text()') not like '%-05-%'
and extractvalue(b.BOOKXML, '/BOOKS/Book/Publish_Date/text()') not like '%-06-%'
and extractvalue(b.BOOKXML, '/BOOKS/Book/Publish_Date/text()') not like '%-07-%'  ;
