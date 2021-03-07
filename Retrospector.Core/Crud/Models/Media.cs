using System.Collections.Generic;
using Retrospector.Core.Crud.Interfaces;

namespace Retrospector.Core.Crud.Models
{
    public class Media : IModel
    {
        public int Id{ get; set; }
        public string Title{ get; set; }
        public string SeasonId{ get; set; }
        public string EpisodeId{ get; set; }
        public string Creator{ get; set; }
        public string Category{ get; set; }
        public MediaType Type{ get; set; }
        public string Description{ get; set; }
        public IEnumerable<Review> Reviews{ get; set; }
        public IEnumerable<Factoid> Factoids{ get; set; }
    }
}