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

