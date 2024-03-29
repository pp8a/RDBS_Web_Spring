-- DML

DELETE FROM EMPLOYEE;
DELETE FROM DEPARTMENT;

INSERT INTO DEPARTMENT VALUES (10, 'ACCOUNTING', 'NEW YORK');
INSERT INTO DEPARTMENT VALUES (20, 'RESEARCH', 'DALLAS');
INSERT INTO DEPARTMENT VALUES (30, 'SALES', 'CHICAGO');
INSERT INTO DEPARTMENT VALUES (40, 'OPERATIONS', 'BOSTON');

INSERT INTO EMPLOYEE VALUES (7839, 'JOHN', 'KING', 'MARIA', 'PRESIDENT', NULL, TO_DATE('17-11-1981', 'DD-MM-YYYY'), 5000, null);
INSERT INTO EMPLOYEE VALUES (7698, 'JOHN', 'BLAKE', 'MARIA', 'MANAGER', 7839, TO_DATE('1-5-1981', 'DD-MM-YYYY'), 2850, 30);
INSERT INTO EMPLOYEE VALUES (7782, 'JOHN', 'CLARK', 'MARIA', 'MANAGER', 7839, TO_DATE('9-6-1981', 'DD-MM-YYYY'), 2450, 10);
INSERT INTO EMPLOYEE VALUES (7566, 'JOHN', 'JONES', 'MARIA', 'MANAGER', 7839, TO_DATE('2-4-1981', 'DD-MM-YYYY'), 2975, 20);
INSERT INTO EMPLOYEE VALUES (7788, 'JOHN', 'SCOTT', 'MARIA', 'ANALYST', 7566, TO_DATE('13-7-87', 'DD-MM-YY'), 3000, 20);
INSERT INTO EMPLOYEE VALUES (7902, 'JOHN', 'FORD', 'MARIA', 'ANALYST', 7566, TO_DATE('3-12-1981', 'DD-MM-YYYY'), 3050, 20);
INSERT INTO EMPLOYEE VALUES (7369, 'JOHN', 'SMITH', 'MARIA', 'CLERK', 7902, TO_DATE('17-12-1980', 'DD-MM-YYYY'), 800, 20);
INSERT INTO EMPLOYEE VALUES (7499, 'JOHN', 'ALLEN', 'MARIA', 'SALESMAN', 7698, TO_DATE('20-2-1981', 'DD-MM-YYYY'), 1600, 30);
INSERT INTO EMPLOYEE VALUES (7521, 'JOHN', 'WARD', 'MARIA', 'SALESMAN', 7698, TO_DATE('22-2-1981', 'DD-MM-YYYY'), 1250, 30);
INSERT INTO EMPLOYEE VALUES (7654, 'JOHN', 'MARTIN', 'MARIA', 'SALESMAN', 7698, TO_DATE('28-9-1981', 'DD-MM-YYYY'), 1300, 30);
INSERT INTO EMPLOYEE VALUES (7844, 'JOHN', 'TURNER', 'MARIA', 'SALESMAN', 7698, TO_DATE('8-9-1981', 'DD-MM-YYYY'), 1500, 30);
INSERT INTO EMPLOYEE VALUES (7876, 'JOHN', 'ADAMS', 'MARIA', 'CLERK', 7788, TO_DATE('13-7-87', 'DD-MM-YY'), 1100, 20);
INSERT INTO EMPLOYEE VALUES (7900, 'JOHN', 'JAMES', 'MARIA', 'CLERK', 7698, TO_DATE('3-12-1981', 'DD-MM-YYYY'), 950, 30);
INSERT INTO EMPLOYEE VALUES (7934, 'JOHN', 'MILLER', 'MARIA', 'CLERK', 7782, TO_DATE('23-1-1982', 'DD-MM-YYYY'), 1200, 10);

COMMIT;
