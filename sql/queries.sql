--****** Modifying Data ******
-- Question
-- The club is adding a new facility - a spa. We need to add it into the facilities table. Use the following values:
-- facid: 9, Name: 'Spa', membercost: 20, guestcost: 30, initialoutlay: 100000, monthlymaintenance: 800.

INSERT INTO cd.facilities (facid, name, membercost,guestcost,initialoutlay, monthlymaintenance)
VALUES(9, 'Spa', 20, 30, 100000, 800);

-- Question
-- This time, though, we want to automatically generate the value for the next facid, rather than specifying it as a constant. Use the following values for everything else:
-- Name: 'Spa', membercost: 20, guestcost: 30, initialoutlay: 100000, monthlymaintenance: 800.

INSERT INTO cd.facilities (facid, name, membercost,guestcost,initialoutlay, monthlymaintenance)
VALUES ((SELECT max(facid) FROM cd.facilities)+1, 'Spa', 20, 30, 100000, 800);

--Question
--We made a mistake when entering the data for the second tennis court. The initial outlay was 10000 rather than 8000: you need to alter the data to fix the error.
UPDATE cd.facilities
SET initialoutlay = 10000
WHERE facid = 1;

--Question
--We want to alter the price of the second tennis court so that it costs 10% more than the first one. Try to do this without using constant values for the prices, so that we can reuse the statement if we want to.
UPDATE cd.facilities
SET
    membercost = court1.memcost * 1.1,
    guestcost = court1.guestcost * 1.1
FROM
    (SELECT membercost as "memcost", guestcost as "guestcost"
     FROM cd.facilities
     WHERE facid = 0) as court1
WHERE
    facid = 1;

--Question
--As part of a clearout of our database, we want to delete all bookings from the cd.bookings table. How can we accomplish this?
DELETE FROM cd.bookings;
--OR use the TRUNCATE statement which is faster because does not write the deletion to the transaction log.
TRUNCATE cd.bookings;

--Question
--We want to remove member 37, who has never made a booking, from our database. How can we achieve that?
DELETE FROM cd.members
WHERE memid = 37;

--****** Basics ******
--Question
--How can you produce a list of facilities that charge a fee to members, and that fee is less than 1/50th of the monthly maintenance cost? Return the facid, facility name, member cost, and monthly maintenance of the facilities in question.
SELECT facid, name, membercost, monthlymaintenance
FROM cd.facilities
WHERE membercost > 0 AND membercost * 50 < monthlymaintenance;

--Question
--How can you produce a list of all facilities with the word 'Tennis' in their name?
SELECT *
FROM cd.facilities
WHERE name LIKE '%Tennis%';

--Question
--How can you retrieve the details of facilities with ID 1 and 5? Try to do it without using the OR operator.
SELECT *
FROM cd.facilities
WHERE facid IN (1,5);

--Question
--How can you produce a list of members who joined after the start of September 2012? Return the memid, surname, firstname, and joindate of the members in question.
SELECT memid, surname, firstname, joindate
FROM cd.members
WHERE joindate >= '2012-09-01';

--Question
--You, for some reason, want a combined list of all surnames and all facility names. Yes, this is a contrived example :-). Produce that list!
SELECT surname FROM cd.members
UNION
SELECT name FROM cd.facilities;

--****** Join ******
--Question
--How can you produce a list of the start times for bookings by members named 'David Farrell'?
SELECT starttime
FROM
	cd.members JOIN cd.bookings ON members.memid = bookings.memid
WHERE
	firstname = 'David' AND surname = 'Farrell';

--Question
--How can you produce a list of the start times for bookings for tennis courts, for the date '2012-09-21'? Return a list of start time and facility name pairings, ordered by the time.
SELECT
	SubQ.starttime AS start,
	SubQ.name AS name
FROM
	(
	 SELECT *
	 FROM
	 	cd.bookings JOIN cd.facilities ON bookings.facid = facilities.facid
	 WHERE
	  	facilities.name LIKE 'Tennis Court%'
	 ) AS SubQ
WHERE
	SubQ.starttime >= '2012-09-21' AND SubQ.starttime < '2012-09-22';

--Question
--How can you output a list of all members, including the individual who recommended them (if any)? Ensure that results are ordered by (surname, firstname).
SELECT
	mem1.firstname as memfname,
	mem1.surname as memsname,
	mem2.firstname as recfname,
	mem2.surname as recsname
FROM
	cd.members mem1 LEFT OUTER JOIN cd.members mem2
	ON mem1.recommendedby = mem2.memid
ORDER BY memsname, memfname;

--Question
--How can you output a list of all members who have recommended another member? Ensure that there are no duplicates in the list, and that results are ordered by (surname, firstname).
SELECT DISTINCT
	m1.firstname as firstname,
	m1.surname as surname
FROM
	(
	 cd.members m1 JOIN cd.members m2
	 ON m1.memid = m2.recommendedby
	)
ORDER BY surname, firstname;

--Question
--How can you output a list of all members, including the individual who recommended them (if any), without using any joins? Ensure that there are no duplicates in the list, and that each firstname + surname pairing is formatted as a column and ordered.
SELECT DISTINCT
	m1.firstname || ' ' || m1.surname as member,
	m2.firstname || ' ' || m2.surname as recommender
FROM
	cd.members m1 LEFT OUTER JOIN cd.members m2
	ON m1.recommendedby = m2.memid
ORDER BY member;

--****** Aggregation ******
--Question
--Produce a count of the number of recommendations each member has made. Order by member ID.
SELECT
	recommendedby,
	count(*)
FROM
	cd.members
WHERE
	recommendedby IS NOT NULL
GROUP BY
	recommendedby
ORDER BY
	recommendedby;

--Question
--Produce a list of the total number of slots booked per facility. For now, just produce an output table consisting of facility id and slots, sorted by facility id.
SELECT
	facid,
	sum(slots) as "Total Slots"
FROM
	cd.bookings
GROUP BY
	facid
ORDER BY
	facid;

--Question
--Produce a list of the total number of slots booked per facility in the month of September 2012. Produce an output table consisting of facility id and slots, sorted by the number of slots.
SELECT
	facid,
	sum(slots) as "Total Slots"
FROM
	cd.bookings
WHERE
	starttime >= '2012-09-01' AND starttime < '2012-10-1'
GROUP BY
	facid
ORDER BY
	"Total Slots";

--Question
--Produce a list of the total number of slots booked per facility per month in the year of 2012. Produce an output table consisting of facility id and slots, sorted by the id and month.
SELECT
	facid,
	EXTRACT(MONTH FROM starttime) as month,
	sum(slots) as "Total Slots"
FROM
	cd.bookings
WHERE
	starttime >= '2012-01-01' AND starttime < '2013-01-01'
GROUP BY
	facid, month
ORDER BY
	facid, month;

--Question
--Find the total number of members (including guests) who have made at least one booking.
SELECT DISTINCT
	count(memid)
FROM
	cd.members
WHERE
	members.memid IN
	(SELECT
		memid
	 FROM
	 	cd.bookings
	);

--Question
--Produce a list of each member name, id, and their first booking after September 1st 2012. Order by member ID.
SELECT
	mem.surname,
	mem.firstname,
	mem.memid,
	MIN(starttime) as "starttime"
FROM
	cd.members mem JOIN cd.bookings book
	ON mem.memid = book.memid
WHERE
	starttime > '2012-09-01'
GROUP BY
	mem.memid
ORDER BY
	mem.memid;

--Question
--Produce a list of member names, with each row containing the total member count. Order by join date, and include guest members.
SELECT
	count(*) OVER(),
	firstname,
	surname
FROM
	cd.members
ORDER BY
	joindate, surname;

--Question
--Produce a monotonically increasing numbered list of members (including guests), ordered by their date of joining. Remember that member IDs are not guaranteed to be sequential.
SELECT
	count(*) OVER(ORDER BY memid),
	firstname,
	surname
FROM
	cd.members;

--Question
--Output the facility id that has the highest number of slots booked. Ensure that in the event of a tie, all tieing results get output.
SELECT
	facid,
	total
FROM
	(
	 SELECT
	   facid,
	   RANK() OVER(ORDER BY SUM(slots) DESC) as "rnk",
	   SUM(slots) as "total"
	 FROM
	   cd.bookings
	 GROUP BY
	   facid
	 ) AS subQ
WHERE
	subQ.rnk = 1;

--****** String ******
--Question
--Output the names of all members, formatted as 'Surname, Firstname'
SELECT
	surname || ', ' || firstname as "name"
FROM
	cd.members;

--Question
--You've noticed that the club's member table has telephone numbers with very inconsistent formatting. You'd like to find all the telephone numbers that contain parentheses, returning the member ID and telephone number sorted by member ID.
SELECT
	memid,
	telephone
FROM
	cd.members
WHERE
	telephone LIKE '(%)%';

--Question
--You'd like to produce a count of how many members you have whose surname starts with each letter of the alphabet. Sort by the letter, and don't worry about printing out a letter if the count is 0.
SELECT
	SUBSTR(surname, 1, 1) as "letter",
	COUNT(*)
FROM
	cd.members
GROUP BY
	letter
ORDER BY
	letter;