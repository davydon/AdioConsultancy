






IF NOT EXISTS(SELECT *

              FROM sys.objects
              WHERE object_id = OBJECT_ID(N'tblApplication') AND type IN (N'U'))
BEGIN
    CREATE TABLE tblApplication
    (

        id              BIGINT  IDENTITY (1, 1) PRIMARY KEY,
        firstName         VARCHAR(100)     NOT NULL  ,
        surName           VARCHAR(100) NOT NULL ,
        email              VARCHAR(100) NOT NULL,
        phoneNumber         VARCHAR(100) NOT NULL,
        coverLetter              VARCHAR(250) NOT NULL,
        passport              VARCHAR(100)  NULL,
        resume       VARCHAR(100)  NULL,

        createdOn            DATETIME     NOT NULL DEFAULT GETDATE(),
        lastUpdatedOn        DATETIME     NULL,




      )

    CREATE NONCLUSTERED  INDEX IX_tblApplication ON tblApplication (firstName,phoneNumber);
    PRINT 'Application Table created successfully'

END
ELSE
BEGIN
    PRINT 'Application table already exist....'
END
GO



-------      Procedures -----------------------------


IF EXISTS(SELECT *
          FROM sys.objects
          WHERE object_id = OBJECT_ID(N'createApplication') AND type IN (N'P', N'PC'))
  DROP PROCEDURE createApplication
GO

CREATE PROCEDURE createApplication
    @id        BIGINT = NULL OUTPUT,
    @firstName  VARCHAR(50),
    @surName     VARCHAR(100) ,
    @email   VARCHAR(100),
    @phoneNumber    VARCHAR(100),
    @coverLetter         VARCHAR(250),
    @passport      VARCHAR(100),
    @resume       VARCHAR(100)


AS
  SET NOCOUNT ON

  INSERT INTO tblApplication
  (
     firstName  ,
    surName      ,
    email   ,
    phoneNumber    ,
    coverLetter         ,
    passport      ,
    resume


  )
  VALUES
    (
    @firstName  ,
    @surName      ,
    @email   ,
    @phoneNumber    ,
    @coverLetter         ,
    @passport      ,
    @resume

    )

  SELECT @id = SCOPE_IDENTITY();

  RETURN @@Error
GO



IF EXISTS(SELECT *
          FROM sys.objects
          WHERE object_id = OBJECT_ID(N'isApplicationExists') AND type IN (N'P', N'PC'))
  DROP PROCEDURE isApplicationExists
GO
CREATE PROCEDURE isApplicationExists
     @firstName  VARCHAR(100),
	@surName VARCHAR(100)

AS
	SET NOCOUNT ON
	 SELECT id FROM tblApplication (nolock) WHERE ( firstName=@firstName  and surName=@surName)
	RETURN @@Error

GO




IF EXISTS(SELECT *
          FROM sys.objects
          WHERE object_id = OBJECT_ID(N'getApplications') AND type IN (N'P', N'PC'))
  DROP PROCEDURE getApplications
GO

CREATE PROCEDURE getApplications
     @page_num  INT = 1,
     @page_size INT = 100
   AS

    SET NOCOUNT ON

       SET @page_num = ABS(@page_num)
       SET @page_size = ABS(@page_size)
       IF @page_num < 1
         SET @page_num = 1
       IF @page_size < 1
         SET @page_size = 1

       SELECT COUNT(*) AS cnt
           FROM tblApplication  (nolock)
       ;WITH pg AS
       (
           SELECT id
           FROM tblApplication  (nolock)

           ORDER BY id
           OFFSET @page_size * (@page_num -1) ROWS
           FETCH NEXT @page_size ROWS ONLY
       )
       SELECT
                                      a.id,
                                     firstName  ,
                                     surName      ,
                                     email   ,
                                     phoneNumber    ,
                                     coverLetter         ,
                                     passport      ,
                                     resume       ,

                                     createdOn,

                                     lastUpdatedOn


        FROM tblApplication  as a (nolock)
        INNER JOIN pg on pg.id = a.id
        ORDER BY pg.id desc
       RETURN @@Error
     GO



