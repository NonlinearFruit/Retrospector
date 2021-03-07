using System;
using Retrospector.DataStorage.Tests.TestDoubles;

namespace Retrospector.DataStorage.Tests.Utilities
{
    public abstract class DatabaseContextTests
    {
        protected DatabaseContext_TestDouble _arrangeContext;
        protected DatabaseContext_TestDouble _actContext;
        protected DatabaseContext_TestDouble _assertContext;

        protected DatabaseContextTests()
        {
            var id = Guid.NewGuid().ToString();
            _arrangeContext = new DatabaseContext_TestDouble(id);
            _actContext = new DatabaseContext_TestDouble(id);
            _assertContext = new DatabaseContext_TestDouble(id);
        }
    }
}