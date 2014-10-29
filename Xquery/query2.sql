select unique extractvalue(b.BOOKXML, '/BOOKS/Book/ID') as "ID"
from Book b, Review r
where extractvalue(r.REVIEWXML, '/REVIEWS/Review/Book_Title/text()') = extractvalue(b.BOOKXML, '/BOOKS/Book/Title/text()')
and extractvalue(r.REVIEWXML, '/REVIEWS/Review/Review_Date/text()')  like '%2014-%';
