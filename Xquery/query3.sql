select unique extractvalue(r.REVIEWXML, '/REVIEWS/Review/Reviewer') as "Reviewer", extractvalue(r.REVIEWXML, '/REVIEWS/Review/Book_Title') as "Book_Title"
from Book b, Review r
order by "Reviewer" ASC, "Book_Title" ASC;
