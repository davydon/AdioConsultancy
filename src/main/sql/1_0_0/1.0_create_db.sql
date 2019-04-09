



USE master
GO


IF EXISTS(SELECT
                name
              FROM sysdatabases
              WHERE name = N'OnlineApplicationDb')
                DROP PROCEDURE OnlineApplicationDb
GO
  BEGIN
    CREATE DATABASE OnlineApplicationDb

 PRINT 'OnlineApplicationDb DB created successfully'
  END
GO







