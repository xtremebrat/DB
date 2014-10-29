select extractvalue(r.REVIEWXML, '/REVIEWS/Review/Reviewer') as "Reviewer", 
        extractvalue(r.REVIEWXML, '/REVIEWS/Review/Rating') as "Rating", 
        extractvalue(b.BOOKXML, '/BOOKS/Book/Publish_Date/text()') as "Publish_Date", 
        extractvalue(r.REVIEWXML, '/REVIEWS/Review/Review_Date/text()') as "Review_Date"
from Book b, Review r
where extractvalue(r.REVIEWXML, '/REVIEWS/Review/Book_Title/text()') = extractvalue(b.BOOKXML, '/BOOKS/Book/Title/text()')
and extractvalue(r.REVIEWXML, '/REVIEWS/Review/Rating') > 3
order by "Publish_Date" ASC, "Review_Date" DESC;
