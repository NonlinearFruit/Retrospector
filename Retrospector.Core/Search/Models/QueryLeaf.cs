namespace Retrospector.Core.Search.Models
{
    public class QueryLeaf
    {
        public Comparator Comparator{ get; set; }
        public RetrospectorAttribute Attribute{ get; set; }
        public string SearchValue{ get; set; }
    }
}