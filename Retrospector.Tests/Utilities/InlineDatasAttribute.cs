using System.Collections.Generic;
using System.Linq;
using System.Reflection;
using Xunit.Sdk;

namespace Retrospector.Tests.Utilities
{
    public class InlineDatasAttribute : DataAttribute
    {
        private readonly object[] _datas;

        public InlineDatasAttribute(params object[] datas)
        {
            _datas = datas;
        }

        public override IEnumerable<object[]> GetData(MethodInfo testMethod) =>
            _datas.Select(data => new []{data});
    }
}