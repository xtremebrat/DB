SELECT R1.Book_Title AS "BOOK TITLE", ROUND(AVG(CAST(R1.rating as float)), 3) AS "AVG RATING"
FROM REVIEW R, 
XMLTABLE('/REVIEWS/Review' passing R.REVIEWXML
                          columns Reviewer VARCHAR(100) PATH 'Reviewer', 
                                  Book_Title VARCHAR(150) PATH 'Book_Title',
                                  Rating VARCHAR(150) PATH 'Rating') R1
group by R1.Book_Title
order by avg(R1.Rating) desc;
