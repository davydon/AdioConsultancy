
 IF NOT EXISTS(SELECT *
                  FROM sys.objects
                  WHERE object_id = OBJECT_ID(N'tblLookup') AND type IN (N'U'))

CREATE TABLE tblLookup (
    id bigint IDENTITY(1,1) NOT NULL primary key,
	lookupType varchar(200) NOT NULL,
	lookupValue varchar(500) NOT NULL,
	lookupDescription varchar(200) NOT NULL,
	lookupSort varchar(10)  NULL,
	status                  INT         NOT NULL DEFAULT 1,
  createdOn            DATETIME     NOT NULL DEFAULT GETDATE(),
  createdBy            BIGINT ,
  lastUpdatedOn        DATETIME     NULL,
  lastUpdatedBy        BIGINT     NULL,
  maker                BIGINT     NULL,
  checker              BIGINT     NULL,
  approved             BIT DEFAULT 0
)
 GO
    CREATE NONCLUSTERED  INDEX IX_tblLookup
        ON tblLookup(lookupType);
GO

