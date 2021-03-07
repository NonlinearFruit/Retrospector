using System.Collections.Generic;

namespace Retrospector.Search.Models
{
    public class QueryTree
    {
        public ICollection<QueryTree> Branches{ get; set; } = new List<QueryTree>();
        public ICollection<QueryLeaf> Leaves{ get; set; } = new List<QueryLeaf>();
        public OperatorType Type{ get; set; }
    }
}