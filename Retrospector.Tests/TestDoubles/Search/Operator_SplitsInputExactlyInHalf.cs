using System.Collections.Generic;
using System.Linq;

namespace Retrospector.Tests.TestDoubles.Search
{
    public class Operator_SplitsInputExactlyInHalf : Operator_TestDouble
    {
        public override IEnumerable<string> Parse(string query)
        {
            return base.Parse(query) ?? SplitInput(query);
        }

        private IEnumerable<string> SplitInput(string query)
        {
            return query.Length % 2 == 0 && query.Any()
                ? new[]
                {
                    query.Substring(query.Length / 2),
                    query.Substring(0, query.Length / 2)
                }
                : new string[0];
        }
    }
}